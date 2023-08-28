package com.soen6841.backend.service.questionnaire;

import com.soen6841.backend.entity.Question;
import com.soen6841.backend.repository.questionnaire.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }
}
