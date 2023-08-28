package com.soen6841.backend.repository;

import com.soen6841.backend.entity.Assignment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends MongoRepository<Assignment, String> {

    @Query("{ doctorId : ?0 }")
    List<Assignment> findAllByDoctorId(String doctorId);

    @Query("{ patientId : ?0 }")
    Assignment findByPatientId(String patientId);
}
