package com.soen6841.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
public class PatientScheduleDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String counselorName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String doctorName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String time;

    public PatientScheduleDto(String id, String counselorName, String doctorName, String time){
        this.id = id;
        this.counselorName = counselorName;
        this.doctorName = doctorName;
        this.time = time;
    }

}
