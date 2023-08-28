package com.soen6841.backend.dto.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionAnswerDetailDto {

    @JsonProperty("question")
    private String questionDescription;
    @JsonProperty("answer")
    private String answer;
}
