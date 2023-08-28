package com.soen6841.backend.service.questionnaire;

import com.soen6841.backend.entity.QuestionOption;
import com.soen6841.backend.repository.questionnaire.QuestionOptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionOptionService {
    private final QuestionOptionRepository questionOptionRepository;

    public QuestionOptionService(QuestionOptionRepository questionOptionRepository) {
        this.questionOptionRepository = questionOptionRepository;
    }

    public List<QuestionOption> findAll() {
        return questionOptionRepository.findAll();
    }
}
