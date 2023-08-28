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
@Document(collection = "status")
public class Status {

    @Id
    @JsonIgnore
    private String id;

    private String patientId;
    private boolean isAssigned;
    private boolean isRejected;
    private boolean isCounselorScheduled;
    private boolean isDoctorScheduled;

    public Status(String patientId, boolean isAssigned, boolean isRejected, boolean isCounselorScheduled, boolean isDoctorScheduled) {
        this.patientId = patientId;
        this.isAssigned = isAssigned;
        this.isRejected = isRejected;
        this.isCounselorScheduled = isCounselorScheduled;
        this.isDoctorScheduled = isDoctorScheduled;
    }

    public void rejectStatus(){
        this.setRejected(true);
    }

    public void unAssignAppointmentLink(){
        this.setAssigned(false);
    }
}
