package com.soen6841.backend.mapper;

import com.soen6841.backend.dto.patients.PatientProfileDto;
import com.soen6841.backend.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.text.SimpleDateFormat;

@Mapper
public interface PatientProfileMapper {
    PatientProfileMapper INSTANCE = Mappers.getMapper(PatientProfileMapper.class);

    default PatientProfileDto map(Account source) {
        if (source == null) {
            return null;
        } else {
            PatientProfileDto patientProfileDto = new PatientProfileDto();
            patientProfileDto.setId(source.getId());
            patientProfileDto.setFirstName(source.getFirstName());
            patientProfileDto.setLastName(source.getLastName());
            patientProfileDto.setEmail(source.getEmail());
            patientProfileDto.setAddress(source.getAddress());
            patientProfileDto.setPhoneNumber(source.getPhoneNumber());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            patientProfileDto.setDateOfBirth(formatter.format(source.getDateOfBirth()));
            return patientProfileDto;
        }
    }

}
