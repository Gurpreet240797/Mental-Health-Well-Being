package com.soen6841.backend.repository.questionnaire;

import com.soen6841.backend.entity.QuestionnaireResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionnaireResultRepository extends MongoRepository<QuestionnaireResult, String> {
    @Query("{ patientId: ?0 }")
    List<QuestionnaireResult> findByPatientId(String patientId);
    List<QuestionnaireResult> findAll();
}
