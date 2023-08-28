package com.soen6841.backend.controller;

import com.soen6841.backend.dto.PatientScheduleDto;
import com.soen6841.backend.dto.account.AccountDto;
import com.soen6841.backend.dto.account.EditDto;
import com.soen6841.backend.dto.questionnaire.*;
import com.soen6841.backend.entity.Account;
import com.soen6841.backend.entity.QuestionAnswer;
import com.soen6841.backend.entity.QuestionnaireResult;
import com.soen6841.backend.mapper.AccountMapper;
import com.soen6841.backend.mapper.QuestionAnswerDetailMapper;
import com.soen6841.backend.mapper.QuestionMapper;
import com.soen6841.backend.mapper.QuestionnaireResultMapper;
import com.soen6841.backend.service.AccountService;
import com.soen6841.backend.service.AppointmentService;
import com.soen6841.backend.service.patients.StatusService;
import com.soen6841.backend.service.questionnaire.QuestionAnswerService;
import com.soen6841.backend.service.questionnaire.QuestionService;
import com.soen6841.backend.service.questionnaire.QuestionnaireResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class PatientController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionnaireResultService questionnaireResultService;
    @Autowired
    private QuestionAnswerService questionAnswerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/questionnaire")

    public List<QuestionDto> getQuestionnaire() {
        return QuestionMapper.INSTANCE.map(questionService.findAll());
    }

    @PostMapping("/questionnaire/submission")
    public QuestionnaireResultDto createQuestionnaireResult(@RequestBody List<QuestionAnswerDto> answers, Authentication authentication, HttpServletResponse response) throws IOException {
        try {
            QuestionnaireResult questionnaireResult = questionnaireResultService.createQuestionnaireResult(answers, authentication.getName());
            questionAnswerService.createQuestionAnswers(answers, questionnaireResult.getId());
            statusService.createOrResetStatus(questionnaireResult.getPatientId());
            return QuestionnaireResultMapper.INSTANCE.map(questionnaireResult);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return null;
        } catch (Exception e) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return null;
        }
    }

    @PostMapping("/edit")
    public AccountDto edit(@RequestBody EditDto editDto, HttpServletResponse response) throws IOException{
        try{
            Account account = accountService.edit(editDto);
            response.setStatus(HttpStatus.OK.value());
            return AccountMapper.INSTANCE.map(account);
        }catch (IllegalArgumentException e){
            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
            return null;
        }
        catch (Exception e){
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return null;
        }
    }

    @GetMapping("/schedule/{id}")
    public PatientScheduleDto getSchedule(@PathVariable String id){
        return appointmentService.getSchedule(id);
    }

    @GetMapping("getResult/{patientId}")
    public List<PatientSelfAssessmentFormDetailsDto> getAssessmentDetails(@PathVariable String patientId, HttpServletResponse response){
        List<PatientSelfAssessmentFormDetailsDto> list = new ArrayList<>();
        try{
            List<QuestionnaireResult> questionnaireResults = questionnaireResultService.findByPatientId(patientId);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            questionnaireResults.forEach(questionnaireResult -> {
                list.add(PatientSelfAssessmentFormDetailsDto.builder().resultId(questionnaireResult.getId()).finishDate(formatter.format(questionnaireResult.getSubmissionDate())).build());
            });
            return list;
        }
        catch (Exception e){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return null;
        }
    }

    @GetMapping("getQuestionAnswers/{resultId}")
    public List<QuestionAnswerDetailDto> getQuestionAnswers(@PathVariable String resultId, HttpServletResponse response){
        try{
            List<QuestionAnswer> questionAnswers = questionAnswerService.getQuestionAnswersByQuestionnaireId(resultId);
            return QuestionAnswerDetailMapper.INSTANCE.map(questionAnswers, questionService.findAll());
        }
        catch (Exception e){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            return null;
        }
    }
}