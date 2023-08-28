package com.soen6841.backend.dto.patients;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PatientInListDto {
    @JsonProperty("patientId")
    private String id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("score")
    private int lastScore;
    @JsonProperty("finishDate")
    private String lastSubmissionDate;
    @JsonProperty("status")
    private String status;
}
