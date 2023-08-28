package com.soen6841.backend.dto;

import com.soen6841.backend.entity.Appointment;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ScheduleDto {
    String pid;

    String patientName;

    String time;

    public ScheduleDto(String pid, String patientName, String time){
        this.pid = pid;
        this.patientName = patientName;
        this.time = time;
    }

}
