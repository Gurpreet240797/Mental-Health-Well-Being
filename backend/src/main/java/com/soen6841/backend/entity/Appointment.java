package com.soen6841.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document("appointment")
public class Appointment {

    @Id
    @JsonIgnore
    private String id;

    private String patientId;
    private String counselorId;
    private String doctorId;
    private String date;
    private String time;

    public Appointment(String patientId, String counselorId, String doctorId, String date, String time) {
        this.patientId = patientId;
        this.counselorId = counselorId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
    }
}
