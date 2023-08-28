package com.soen6841.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "questionnaireResult")
public class QuestionnaireResult {

    @Id
    @JsonIgnore
    private String id;

    private String patientId;
    private Date submissionDate;
    private int totalScore;

    public QuestionnaireResult(String patientId, Date submissionDate, int totalScore) {
        this.patientId = patientId;
        this.submissionDate = submissionDate;
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return "submitted on " + formatter.format(submissionDate) + " with a total score of " + totalScore;
    }
}
