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
@Document(collection = "assignment")
public class Assignment {

    @Id
    @JsonIgnore
    private String id;

    private String patientId;
    private String doctorId;

    public Assignment(String patientId, String doctorId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
    }
}
