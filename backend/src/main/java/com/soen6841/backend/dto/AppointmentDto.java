package com.soen6841.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppointmentDto {
    @JsonProperty(value = "patientId")
    String patientId;
    @JsonProperty(value = "currentId")
    @JsonAlias({"counselorId","doctorId"})
    String currentId;
    @JsonProperty(value = "date")
    String date;
    @JsonProperty(value = "time")
    String time;

    @JsonIgnore
    public String getPatientId(){return patientId;}
    @JsonIgnore
    public void setPatientId(String patientId){this.patientId=patientId;}
    @JsonIgnore
    public String getCurrentId(){return currentId;}
    @JsonIgnore
    public void setCurrentId(String currentId){this.currentId=currentId;}
    @JsonIgnore
    public String getDate(){return date;}
    @JsonIgnore
    public void setDate(String date){this.date=date;}
    @JsonIgnore
    public String getTime(){return time;}
    @JsonIgnore
    public void setTime(String time){this.time=time;}
}
