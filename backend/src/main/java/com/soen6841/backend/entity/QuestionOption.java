package com.soen6841.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "questionOption")
public class QuestionOption {

    @Id
    @JsonIgnore
    private String id;

    private String optionString;
    private int score;

    public QuestionOption(String optionString, int score) {
        this.optionString = optionString;
        this.score = score;
    }
}
