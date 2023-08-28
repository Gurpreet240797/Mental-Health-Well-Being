package com.soen6841.backend.repository.patients;

import com.soen6841.backend.entity.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends MongoRepository<Status, String> {

    @Query("{ isRejected : false, isAssigned : false, isCounselorScheduled : false, isDoctorScheduled : false}")
    List<Status> findAllEmptyStatus();

    @Query("{ isRejected : false, isAssigned : true}")
    List<Status> findAllAssignedStatus();

    @Query("{ isRejected : false, isAssigned : false, isCounselorScheduled : true, isDoctorScheduled : false}")
    List<Status> findAllCounselorScheduledStatus();

    @Query("{ isRejected : false, isAssigned : true, isCounselorScheduled : false, isDoctorScheduled : true}")
    List<Status> findAllDoctorScheduledStatus();

    @Query("{ isRejected : true}")
    List<Status> findAllRejectedStatus();

    @Query("{ patientId : ?0 }")
    Status findByPatientId(String patientId);

}
