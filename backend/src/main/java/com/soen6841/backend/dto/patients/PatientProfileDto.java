package com.soen6841.backend.dto.patients;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PatientProfileDto {

    @JsonProperty("pid")
    private String id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("address")
    private String address;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
}
