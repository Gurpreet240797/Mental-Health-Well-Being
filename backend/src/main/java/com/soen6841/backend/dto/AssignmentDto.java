package com.soen6841.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentDto {

    @JsonProperty("patientId")
    String patientId;
    @JsonProperty("doctorId")
    String doctorId;

}
