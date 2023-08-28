package com.soen6841.backend.service;

import com.soen6841.backend.entity.Status;
import com.soen6841.backend.entity.Assignment;
import com.soen6841.backend.repository.AssignmentRepository;
import com.soen6841.backend.repository.patients.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    StatusRepository statusRepository;

    public List<String> getAllPatientIdsByDoctorId(String doctorId) {
        List<Assignment> assignments = assignmentRepository.findAllByDoctorId(doctorId);
        return assignments.stream().map(Assignment::getPatientId).collect(Collectors.toList());
    }

    public void assignPatientToDoctor(String patientId, String doctorId) throws Exception {
        Assignment assignment = new Assignment();
        assignment.setPatientId(patientId);
        assignment.setDoctorId(doctorId);
        Status status = statusRepository.findByPatientId(patientId);
        if (status == null) {
            throw new Exception("This patient may not exist in our system or hasn't done any questionnaire yet.");
        }
        if (!status.isAssigned() && !status.isDoctorScheduled() && !status.isRejected() && !status.isCounselorScheduled()) {
            status.setAssigned(true);
            statusRepository.save(status);
            assignmentRepository.save(assignment);
        } else {
            throw new Exception("This patient could not be assigned due to current status.");
        }
    }

    public void removeAssignment(String patientId) {
        Assignment assignment = assignmentRepository.findByPatientId(patientId);
        if (assignment != null) {
            assignmentRepository.delete(assignment);
        }
    }
}
