package com.soen6841.backend.service.patients;

import com.soen6841.backend.entity.Account;
import ch.qos.logback.core.encoder.EchoEncoder;
import com.soen6841.backend.entity.Appointment;
import com.soen6841.backend.entity.Status;
import com.soen6841.backend.repository.patients.StatusRepository;
import com.soen6841.backend.service.AppointmentService;
import com.soen6841.backend.service.questionnaire.QuestionnaireResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatusService {

    private final StatusRepository statusRepository;

    public void createOrResetStatus(String patientId) {
        Status status = statusRepository.findByPatientId(patientId);
        if (status == null) {
            status = new Status(patientId, false, false, false, false);
        }

        if (status.isRejected()) {
            resetStatus(status);
        }
        statusRepository.save(status);
    }

    private void resetStatus(Status status) {
        status.setRejected(false);
        status.setAssigned(false);
        status.setCounselorScheduled(false);
        status.setDoctorScheduled(false);
    }

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public List<Status> findAllEmptyStatus() {
        return statusRepository.findAllEmptyStatus();
    }

    public List<Status> findAllAssignedStatus() {
        return statusRepository.findAllAssignedStatus();
    }

    public List<Status> findAllCounselorScheduledStatus() {
        return statusRepository.findAllCounselorScheduledStatus();
    }

    public List<Status> findAllDoctorScheduledStatus() {
        return statusRepository.findAllDoctorScheduledStatus();
    }

    public List<Status> findAllRejectedStatus() {
        return statusRepository.findAllRejectedStatus();
    }

    public List<String> getAllEmptyStatusPatientIds() {
        List<Status> emptyStatus = statusRepository.findAllEmptyStatus();
        return emptyStatus.stream().map(Status::getPatientId).collect(Collectors.toList());
    }

    public List<String> getAllAssignedPatientIds() {
        List<Status> assignedStatus = statusRepository.findAllAssignedStatus();
        return assignedStatus.stream().map(Status::getPatientId).collect(Collectors.toList());
    }

    public List<String> getAllCounselorScheduledPatientIds() {
        List<Status> counselorScheduledStatus = statusRepository.findAllCounselorScheduledStatus();
        return counselorScheduledStatus.stream().map(Status::getPatientId).collect(Collectors.toList());
    }

    public List<String> getAllDoctorScheduledPatientIds() {
        List<Status> doctorScheduledStatus = statusRepository.findAllDoctorScheduledStatus();
        return doctorScheduledStatus.stream().map(Status::getPatientId).collect(Collectors.toList());
    }

    public List<String> getAllRejectedPatientIds() {
        List<Status> rejectedStatus = statusRepository.findAllRejectedStatus();
        return rejectedStatus.stream().map(Status::getPatientId).collect(Collectors.toList());
    }

    public List<Status> getStatusByPatientIds(List<Account> patients) {
        List<Status> statuses = new ArrayList<>(patients.size());
        for (Account patient : patients) {
            statuses.add(statusRepository.findByPatientId(patient.getId()));
        }
        return statuses;
    }


    public Status rejectAppointmentStatus(String patientId) throws Exception {
            Status status = statusRepository.findByPatientId(patientId);
            if(status.isCounselorScheduled() || status.isDoctorScheduled()) throw new Exception("Appointment is Already Scheduled");
            status = rejectStatusAndSave(status);
            return status;
    }

    private Status rejectStatusAndSave(Status status){
        status.rejectStatus();
        status.unAssignAppointmentLink();
        statusRepository.save(status);
        return status;
    }

    public void removePatientStatus(String patientId) {
        Status status = statusRepository.findByPatientId(patientId);
        if (status != null) {
            statusRepository.delete(status);
        }
    }

}
