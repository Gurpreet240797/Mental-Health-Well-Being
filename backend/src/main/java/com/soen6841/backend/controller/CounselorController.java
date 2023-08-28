package com.soen6841.backend.controller;

import com.soen6841.backend.constant.Role;
import com.soen6841.backend.dto.AppointmentDto;
import com.soen6841.backend.dto.ScheduleDto;
import com.soen6841.backend.dto.account.AccountDto;
import com.soen6841.backend.dto.AssignmentDto;
import com.soen6841.backend.dto.AvailableTimeDto;
import com.soen6841.backend.dto.account.EditDto;
import com.soen6841.backend.dto.patients.PatientInListDto;
import com.soen6841.backend.dto.patients.PatientProfileDto;
import com.soen6841.backend.dto.questionnaire.QuestionAnswerDetailDto;
import com.soen6841.backend.entity.*;
import com.soen6841.backend.mapper.AccountMapper;
import com.soen6841.backend.mapper.PatientInListMapper;
import com.soen6841.backend.mapper.PatientProfileMapper;
import com.soen6841.backend.mapper.QuestionAnswerDetailMapper;
import com.soen6841.backend.service.AccountService;
import com.soen6841.backend.service.AppointmentService;
import com.soen6841.backend.service.AssignmentService;
import com.soen6841.backend.service.EmailService;
import com.soen6841.backend.service.patients.StatusService;
import com.soen6841.backend.service.questionnaire.QuestionAnswerService;
import com.soen6841.backend.service.questionnaire.QuestionService;
import com.soen6841.backend.service.questionnaire.QuestionnaireResultService;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/counselor")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CounselorController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private QuestionnaireResultService questionnaireResultService;
    @Autowired
    private QuestionAnswerService questionAnswerService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/getPatients")
    public List<PatientInListDto> getAllPatients(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<Account> patients = accountService.getPatientsForCounselor(accountService.getAccountByEmail(username));
            List<QuestionnaireResult> questionnaireResults = questionnaireResultService.getAllPatientsLatestResults(patients);
            List<Status> patientStatus = statusService.getStatusByPatientIds(patients);
            return PatientInListMapper.INSTANCE.map(patients, questionnaireResults, patientStatus);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @GetMapping("/getPatient/{id}")
    public PatientProfileDto getPatientProfile(@PathVariable String id) {
        Optional<Account> patient = accountService.getAccountById(id);
        return patient.map(PatientProfileMapper.INSTANCE::map).orElse(null);
    }

    @GetMapping("/getPatient/{id}/questionnaire")
    public List<QuestionAnswerDetailDto> getPatientLatestAnswer(@PathVariable String id) {
        try {
            List<QuestionAnswer> answers = questionAnswerService.getLatestAnswersByQuestionnaire(id);
            return QuestionAnswerDetailMapper.INSTANCE.map(answers, questionService.findAll());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @GetMapping("/getAllDoctors")
    public List<AccountDto> getAllDoctors(){
        return AccountMapper.INSTANCE.mapList(accountService.getAllDoctors());
    }

    @PostMapping("/assignPatientToDoctor")
    public void assignPatientToDoctor(@RequestBody AssignmentDto assignmentDto, HttpServletResponse response) throws IOException {
        try{
            assignmentService.assignPatientToDoctor(assignmentDto.getPatientId(),assignmentDto.getDoctorId());
            response.setStatus(HttpStatus.CREATED.value());
        } catch (Exception e){
            response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }



    @PostMapping("/offerAppointment")
    public void offerAppointment(@RequestBody AppointmentDto appointmentDto, HttpServletResponse response) throws IOException{
        try{
            Appointment appointment = appointmentService.offerAppointment(appointmentDto, Role.COUNSELOR);
            response.setStatus(HttpStatus.CREATED.value());

            Optional<Account> patient = accountService.getAccountById(appointmentDto.getPatientId());
            String counselorId = appointment.getCounselorId();
            Optional<Account> counselor = accountService.getAccountById(counselorId);
            if (patient.isPresent() && counselor.isPresent()) {
                String appointmentWith = counselor.get().getFirstName() + " " + counselor.get().getLastName();
                String body = "Dear " + patient.get().getFirstName() + " " + patient.get().getLastName() + ",\n\n"
                        + "You have a new appointment with Counselor: " + appointmentWith + " at " + appointment.getDate() + ", " + appointment.getTime() + ".\n\n"
                        + "Thank you,\n"
                        + "The MoodSpace Team";
                emailService.sendEmail(patient.get().getEmail(), "New Appointment has been scheduled", body);
            }
        }catch (Exception e){
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/getAvailableTime")
    @ResponseBody
    public List<AvailableTime> getAvailableTime(@RequestBody AvailableTimeDto availableTimeDto, HttpServletResponse response) throws IOException{
        try{
            return appointmentService.getAvailableTimes(availableTimeDto, Role.COUNSELOR);
        }catch (Exception e){
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return null;
    }



    @PostMapping("/rejectPatient")
    public String rejectPatient(@RequestParam(name = "patientId") String patientId, HttpServletResponse httpServletResponse){
        try {
            statusService.rejectAppointmentStatus(patientId);
            httpServletResponse.setStatus(HttpStatus.OK.value());

            Optional<Account> patient = accountService.getAccountById(patientId);
            if (patient.isPresent()) {
                QuestionnaireResult questionnaireResult = questionnaireResultService.getPatientLatestResult(patient.get());
                if (questionnaireResult != null) {
                    String body = "Dear " + patient.get().getFirstName() + " " + patient.get().getLastName() + ",\n\n"
                            + "Your questionnaire: " + questionnaireResult + " has been rejected by the counselor.\n\n"
                            + "Thank you,\n"
                            + "The MoodSpace Team";
                    emailService.sendEmail(patient.get().getEmail(), "Your Questionnaire Result has been rejected", body);
                }
            }

            return "Appointment Rejected Successfully";
        }
        catch (Exception e){
            httpServletResponse.setStatus(HttpStatus.EXPECTATION_FAILED.value());
            return "Appointment Rejection UnSuccessful";
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
    public List<ScheduleDto> getSchedule(@PathVariable String id){
        return appointmentService.getSchedule(id, Role.COUNSELOR);
    }

}
