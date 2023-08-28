package com.soen6841.backend.dto.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionAnswerDto {
    @JsonProperty("qid")
    private String questionId;
    @JsonProperty("score")
    private int score;
}
