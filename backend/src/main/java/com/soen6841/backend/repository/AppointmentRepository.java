package com.soen6841.backend.repository;

import com.soen6841.backend.entity.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    @Query("{ counselorId : ?0 }")
    List<Appointment> findAllByCounselorId(String counselorId);

    @Query("{ counselorId : ?0, date : ?1 }")
    List<Appointment> findAllTimeByCounselorId(String counselorId, String date);

    @Query("{ doctorId : ?0 }")
    List<Appointment> findAllByDoctorId(String doctorId);

    @Query("{ doctorId : ?0, date : ?1 }")
    List<Appointment> findAllTimeByDoctorId(String doctorId, String date);

    @Query("{ patientId : ?0 }")
    Appointment findByPatientId(String patientId);

    @Query(value = "{ patientID : ?0 }", delete = true)
    Appointment deleteByPatientId(String patientId);

}
