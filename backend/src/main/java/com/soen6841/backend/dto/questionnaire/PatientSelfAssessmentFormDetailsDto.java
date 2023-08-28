package com.soen6841.backend.dto.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientSelfAssessmentFormDetailsDto {

    @JsonProperty("resultId")
    private String resultId;

    @JsonProperty("finishDate")
    private String finishDate;

}
