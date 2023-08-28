package com.soen6841.backend.mapper;

import com.soen6841.backend.dto.patients.PatientInListDto;
import com.soen6841.backend.entity.Account;
import com.soen6841.backend.entity.QuestionnaireResult;
import com.soen6841.backend.entity.Status;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Mapper
public interface PatientInListMapper {
    PatientInListMapper INSTANCE = Mappers.getMapper(PatientInListMapper.class);

    default PatientInListDto map(Account patientSource, QuestionnaireResult resultSource, Status statusSource) {
        if ( patientSource == null ) {
            return null;
        }

        PatientInListDto patientInListDto = new PatientInListDto();
        patientInListDto.setId( patientSource.getId() );
        patientInListDto.setFirstName( patientSource.getFirstName() );
        patientInListDto.setLastName( patientSource.getLastName() );
        patientInListDto.setLastScore( resultSource.getTotalScore() );
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        patientInListDto.setLastSubmissionDate( formatter.format(resultSource.getSubmissionDate()) );
        if ( statusSource.isDoctorScheduled() || statusSource.isCounselorScheduled() ) {
            patientInListDto.setStatus("Appointment");
        } else {
            patientInListDto.setStatus("");
        }

        return patientInListDto;
    }

    default List<PatientInListDto> map(List<Account> patientSource, List<QuestionnaireResult> questionnaireResults, List<Status> scheduledStatus) {
        if ( patientSource != null && questionnaireResults != null ) {
            if ( patientSource.size() != questionnaireResults.size()) {
                throw new IllegalArgumentException("patientSource and questionnaireResults must have the same size");
            }

            List<PatientInListDto> list = new ArrayList<>( patientSource.size() );
            for (Account patient : patientSource) {
                Optional<QuestionnaireResult> result = questionnaireResults.stream().filter(r -> r.getPatientId().equals(patient.getId())).findFirst();
                if (result.isPresent()) {
                    Optional<Status> status = scheduledStatus.stream().filter(s -> s.getPatientId().equals(patient.getId())).findFirst();
                    list.add(map(patient, result.get(), status.orElse(null)));
                }
            }
            return list;
        }

        return Collections.emptyList();
    }
}
