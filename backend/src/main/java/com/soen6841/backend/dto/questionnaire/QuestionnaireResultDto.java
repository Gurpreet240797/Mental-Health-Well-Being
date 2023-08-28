package com.soen6841.backend.dto.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class QuestionnaireResultDto {
    @JsonProperty("qnId")
    private String id;
    @JsonProperty("submissionDate")
    private Date submissionDate;
    @JsonProperty("totalScore")
    private int totalScore;
}
