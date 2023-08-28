package com.soen6841.backend.service.questionnaire;

import com.soen6841.backend.dto.questionnaire.QuestionAnswerDto;
import com.soen6841.backend.entity.Account;
import com.soen6841.backend.entity.QuestionnaireResult;
import com.soen6841.backend.repository.AccountRepository;
import com.soen6841.backend.repository.questionnaire.QuestionnaireResultRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionnaireResultService {
    private final QuestionnaireResultRepository questionnaireResultRepository;
    private final AccountRepository accountRepository;

    public QuestionnaireResultService(QuestionnaireResultRepository questionnaireResultRepository, AccountRepository accountRepository) {
        this.questionnaireResultRepository = questionnaireResultRepository;
        this.accountRepository = accountRepository;
    }

    public List<QuestionnaireResult> findByPatientId(String patientId) {
        return questionnaireResultRepository.findByPatientId(patientId);
    }

    public QuestionnaireResult createQuestionnaireResult(List<QuestionAnswerDto> answers, String username) {
        if (answers.size() != 9) {
            throw new IllegalArgumentException("Please select an option for all questions");
        }

        QuestionnaireResult questionnaireResult = new QuestionnaireResult();
        questionnaireResult.setPatientId(accountRepository.findByEmail(username).getId());
        questionnaireResult.setSubmissionDate(new Date());
        questionnaireResult.setTotalScore(calculateTotalScore(answers));
        return questionnaireResultRepository.save(questionnaireResult);
    }

    private int calculateTotalScore(List<QuestionAnswerDto> answers) {
        int totalScore = 0;
        for (QuestionAnswerDto answer : answers) {
            totalScore += answer.getScore();
        }
        return totalScore;
    }

    public List<QuestionnaireResult> getAllPatientsLatestResults(List<Account> patients) {
        List<QuestionnaireResult> results = new ArrayList<>(patients.size());
        for (Account patient : patients) {
            results.add(getPatientLatestResult(patient));
        }
        return results;
    }

    public QuestionnaireResult getPatientLatestResult(Account patient) {
        return getPatientLatestResultByPatientId(patient.getId());
    }

    public QuestionnaireResult getPatientLatestResultByPatientId(String patientId) {
        List<QuestionnaireResult> patientResults = questionnaireResultRepository.findByPatientId(patientId);
        if (patientResults.isEmpty()) {
            return null;
        }
        QuestionnaireResult latestResult = patientResults.get(0);
        for (QuestionnaireResult result : patientResults) {
            if (result.getSubmissionDate().after(latestResult.getSubmissionDate())) {
                latestResult = result;
            }
        }
        return latestResult;
    }

    public Optional<QuestionnaireResult> findById(String id){
        return questionnaireResultRepository.findById(id);
    }
}
