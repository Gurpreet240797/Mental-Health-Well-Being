package com.soen6841.backend.repository.questionnaire;

import com.soen6841.backend.entity.QuestionAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionAnswerRepository extends MongoRepository<QuestionAnswer, String> {

    @Query("{ questionnaireId: ?0 }")
    List<QuestionAnswer> findByQuestionId(String questionnaireId);
}
