package com.soen6841.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "question")
public class Question {

    @Id
    @JsonIgnore
    private String id;

    private String questionString;
    @DBRef
    private List<QuestionOption> options;

    public Question(String questionString, List<QuestionOption> options) {
        this.questionString = questionString;
        this.options = options;
    }
}
