package com.soen6841.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "questionAnswer")
public class QuestionAnswer {

    @Id
    @JsonIgnore
    private String id;

    private String questionnaireId;
    private String questionId;
    private int score;

    public QuestionAnswer(String questionnaireId, String questionId, int score) {
        this.questionnaireId = questionnaireId;
        this.questionId = questionId;
        this.score = score;
    }
}
