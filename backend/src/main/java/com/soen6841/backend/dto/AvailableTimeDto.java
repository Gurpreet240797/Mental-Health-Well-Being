package com.soen6841.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AvailableTimeDto {
    @JsonProperty("currentId")
    @JsonAlias({"counselorId","doctorId"})
    String currentId;

    String date;
}
