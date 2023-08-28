package com.soen6841.backend.service;

import com.soen6841.backend.constant.Role;
import com.soen6841.backend.dto.AppointmentDto;
import com.soen6841.backend.dto.AvailableTimeDto;
import com.soen6841.backend.dto.PatientScheduleDto;
import com.soen6841.backend.dto.ScheduleDto;
import com.soen6841.backend.entity.Account;
import com.soen6841.backend.entity.Appointment;
import com.soen6841.backend.entity.AvailableTime;
import com.soen6841.backend.entity.Status;
import com.soen6841.backend.repository.AccountRepository;
import com.soen6841.backend.repository.AppointmentRepository;
import com.soen6841.backend.repository.patients.StatusRepository;
import com.soen6841.backend.service.patients.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StatusService statusService;
    @Autowired
    private AssignmentService assignmentService;

    public List<String> getAllPatientIdsByCounselorId(String counselorId) {
        List<Appointment> appointments = appointmentRepository.findAllByCounselorId(counselorId);
        List<String> patientIds = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (earlier(appointment.getDate(), appointment.getTime())) {
                statusService.removePatientStatus(appointment.getPatientId());
            } else {
                patientIds.add(appointment.getPatientId());
            }
        }
        return patientIds;
    }

    public void completeDoctorAppointment(String doctorId) {
        List<Appointment> appointments = appointmentRepository.findAllByDoctorId(doctorId);
        for (Appointment appointment : appointments) {
            if (earlier(appointment.getDate(), appointment.getTime())) {
                statusService.removePatientStatus(appointment.getPatientId());
                assignmentService.removeAssignment(appointment.getPatientId());
            }
        }
    }

    public Appointment offerAppointment(AppointmentDto appointmentDto, Role role){
        String patientId = appointmentDto.getPatientId();
        String counselorId = "";
        String doctorId = "";
        Status status = statusRepository.findByPatientId(patientId);
        if(role == Role.DOCTOR){
            doctorId = appointmentDto.getCurrentId();
            status.setDoctorScheduled(true);
        }else{
            counselorId = appointmentDto.getCurrentId();
            status.setCounselorScheduled(true);
        }
        Appointment appointment = new Appointment(patientId, counselorId, doctorId, appointmentDto.getDate(), appointmentDto.getTime());
        appointmentRepository.save(appointment);
        statusRepository.save(status);
        return appointment;
    }

    public List<AvailableTime> getAvailableTimes(AvailableTimeDto availableTimeDto, Role role){
        List<Appointment> appointments;
        if(role == Role.DOCTOR){
            appointments = appointmentRepository.findAllTimeByDoctorId(availableTimeDto.getCurrentId(), availableTimeDto.getDate());
        }else{
            appointments = appointmentRepository.findAllTimeByCounselorId(availableTimeDto.getCurrentId(), availableTimeDto.getDate());
        }
        List<String> times = appointments.stream().map(Appointment::getTime).sorted().collect(Collectors.toList());
        List<AvailableTime> availableTimes = new ArrayList<>();
        int index = 0;
        for(int i=9;i<=17;i++){
            String temp = String.format("%d:00", i);
            if(times.contains(temp)) continue;
            availableTimes.add(new AvailableTime(String.valueOf(index++), temp));
        }
        return availableTimes;
    }

    public List<ScheduleDto> getSchedule(String id, Role role){
        List<ScheduleDto> schedules;
        List<Appointment> appointments;
        if(role == Role.DOCTOR){
            appointments = appointmentRepository.findAllByDoctorId(id);
        }else if(role == Role.COUNSELOR){
            appointments = appointmentRepository.findAllByCounselorId(id);
        }else return null;
        schedules = appointments.stream()
                .filter(x -> x.getDate() != null && x.getTime() != null)
                .filter(x -> !earlier(x.getDate(), x.getTime()))
                .sorted(new Comparator<Appointment>(){
                    public int compare(Appointment a1, Appointment a2){
                        return formatTime(a1.getDate(),a1.getTime()).compareTo(formatTime(a2.getDate(), a2.getTime()));
                    }
                })
                .map(x -> new ScheduleDto(x.getPatientId(), getNameById(x.getPatientId()), x.getDate() + " " + x.getTime()))
                .collect(Collectors.toList());
        return schedules;
    }

    public PatientScheduleDto getSchedule(String id){
        Appointment appointment = appointmentRepository.findByPatientId(id);
        if(appointment == null || earlier(appointment.getDate(), appointment.getTime())) {
            return new PatientScheduleDto();
        }
        String counselorId = appointment.getCounselorId();
        String doctorId = appointment.getDoctorId();
        String counselorName = "";
        String doctorName = "";
        String id_return = "";
        if(counselorId != null && !counselorId.isEmpty()){
            id_return = counselorId;
            counselorName = getNameById(counselorId);
        }else if(doctorId != null && !doctorId.isEmpty()){
            id_return = doctorId;
            doctorName = getNameById(doctorId);
        }
        return new PatientScheduleDto(id_return, counselorName, doctorName, appointment.getDate() + " " + appointment.getTime());
    }

    public String getNameById(String id){
        Optional<Account> account = accountRepository.findById(id);
        return account.map(value -> value.getFirstName() + " " + value.getLastName()).orElse("");
    }

    public boolean earlier(String date, String time){
        try{
            Date current = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date appointment = formatter.parse(formatTime(date, time));
            if(appointment.before(current)) return true;
            return false;
        }catch(Exception e){
            return false;
        }
    }

    public String formatTime(String date, String time){
        String[] dates = date.split("/");
        String[] times = time.split(":");
        return String.format("%04d-%02d-%02d %02d:%02d",
                Integer.valueOf(dates[0]), Integer.valueOf(dates[1]), Integer.valueOf(dates[2]),
                Integer.valueOf(times[0]), Integer.valueOf(times[1]));
    }

    public void finishAppointment(AppointmentDto appointmentDto, Role role){
        String patientId = appointmentDto.getPatientId();
        statusService.createOrResetStatus(patientId);
        appointmentRepository.deleteByPatientId(patientId);
    }

    public Appointment getAppointmentByPatientId(String patientId){
        return appointmentRepository.findByPatientId(patientId);
    }

}
