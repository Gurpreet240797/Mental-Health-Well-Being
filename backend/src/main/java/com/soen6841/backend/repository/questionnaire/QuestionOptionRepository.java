package com.soen6841.backend.repository.questionnaire;

import com.soen6841.backend.entity.QuestionOption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionOptionRepository extends MongoRepository<QuestionOption, String> {
    List<QuestionOption> findAll();
}
