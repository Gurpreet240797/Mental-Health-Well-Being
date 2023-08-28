package com.soen6841.backend.service.questionnaire;

import com.soen6841.backend.dto.questionnaire.QuestionAnswerDto;
import com.soen6841.backend.entity.Account;
import com.soen6841.backend.entity.QuestionAnswer;
import com.soen6841.backend.entity.QuestionnaireResult;
import com.soen6841.backend.repository.questionnaire.QuestionAnswerRepository;
import com.soen6841.backend.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionAnswerService {
    private final QuestionAnswerRepository questionAnswerRepository;
    private final AccountService accountService;
    private final QuestionnaireResultService questionnaireResultService;

    public QuestionAnswerService(QuestionAnswerRepository questionAnswerRepository, AccountService accountService,
                                 QuestionnaireResultService questionnaireResultService) {
        this.questionAnswerRepository = questionAnswerRepository;
        this.accountService = accountService;
        this.questionnaireResultService = questionnaireResultService;
    }

    public List<QuestionAnswer> getLatestAnswersByQuestionnaire(String patientId) {
        Optional<Account> patient = accountService.getAccountById(patientId);
        if (patient.isPresent()) {
            QuestionnaireResult questionnaireResult = questionnaireResultService.getPatientLatestResult(patient.get());
            if (questionnaireResult != null) {
                String questionnaireId = questionnaireResult.getId();
                return questionAnswerRepository.findByQuestionId(questionnaireId);
            }
        }
        return Collections.emptyList();
    }

    public List<QuestionAnswer> getQuestionAnswersByQuestionnaireId(String questionnaireId){
        return questionAnswerRepository.findByQuestionId(questionnaireId);
    }

    public void createQuestionAnswers(List<QuestionAnswerDto> answers, String questionnaireId) {
        for (QuestionAnswerDto answer : answers) {
            QuestionAnswer questionAnswer = new QuestionAnswer();
            questionAnswer.setQuestionId(answer.getQuestionId());
            questionAnswer.setQuestionnaireId(questionnaireId);
            questionAnswer.setScore(answer.getScore());
            questionAnswerRepository.save(questionAnswer);
        }
    }

    public Optional<QuestionAnswer> findById(String id){
        return questionAnswerRepository.findById(id);
    }


}
