package com.soen6841.backend.dto.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionOptionDto {
    @JsonProperty("optionStr")
    private String optionString;
    @JsonProperty("score")
    private int score;
}
