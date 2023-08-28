package com.soen6841.backend.service;

import com.soen6841.backend.constant.Role;
import com.soen6841.backend.dto.account.EditDto;
import com.soen6841.backend.dto.account.RegisterDto;
import com.soen6841.backend.dto.UserData;
import com.soen6841.backend.entity.Account;
import com.soen6841.backend.entity.Appointment;
import com.soen6841.backend.entity.QuestionnaireResult;
import com.soen6841.backend.repository.AccountRepository;
import com.soen6841.backend.service.patients.StatusService;
import com.soen6841.backend.service.questionnaire.QuestionnaireResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import com.soen6841.backend.entity.CurrentUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Account login(String email, String password, HttpServletRequest request) throws ServletException {
        //SecurityContextHolder.clearContext();
        request.login(email, password);// trigger spring security authentication
        return (Account) RequestContextHolder.getRequestAttributes().getAttribute("loginAccount", RequestAttributes.SCOPE_REQUEST);
    }

    public void logout(HttpServletRequest request) throws ServletException {
        request.logout();
    }


    public void createAccount(RegisterDto registerDto){
        if(!EmailValidator.getInstance().isValid(registerDto.getEmail())){
            throw new IllegalArgumentException("Email is not valid.");
        }
        Account account = accountRepository.findByEmail(registerDto.getEmail());
        if(Objects.nonNull(account)){
            throw new IllegalArgumentException("Email is already used.");
        }
        if(registerDto.getFirstName().isEmpty() || registerDto.getFirstName() == null){
            throw new IllegalArgumentException("First name should not be empty or null.");
        }
        if(registerDto.getLastName().isEmpty() || registerDto.getLastName() == null){
            throw new IllegalArgumentException("Last name should not be empty or null");
        }
        if(registerDto.getAddress().isEmpty() || registerDto.getAddress() == null){
            throw new IllegalArgumentException("Address should not be empty or null");
        }
        if(registerDto.getDateOfBirth() == null){
            throw new IllegalArgumentException("Date of birth should not be null.");
        }
        if(registerDto.getPhoneNumber().isEmpty() || registerDto.getPhoneNumber() == null){
            throw new IllegalArgumentException("Phone number should not be empty or null.");
        }
        Role role;
        switch (registerDto.getRole()){
            case 2: role = Role.PATIENT;break;
            case 3: role = Role.DOCTOR;break;
            case 4: role = Role.COUNSELOR;break;
            default: role = null;
        }
        if(registerDto.getRole()!=2){
            if(Objects.nonNull(accountRepository.findByRegistrationNumber(registerDto.getRegistrationNumber()))){
                throw new IllegalArgumentException("Registration Number is already used.");
            }
        }
        String encodePassword = bCryptPasswordEncoder.encode(registerDto.getPassword());
        account = new Account(registerDto.getEmail(), encodePassword, registerDto.getFirstName(), registerDto.getLastName(), registerDto.getDateOfBirth(), role, registerDto.getRegistrationNumber(), registerDto.getAddress(), registerDto.getPhoneNumber());
        accountRepository.save(account);
    }


    public void deleteAccount(String emailAddress){
        accountRepository.deleteByEmail(emailAddress);
    }

    public List<Account> getPatientsForCounselor(Account counselor) {
        List<String> appointmentPatientIds = appointmentService.getAllPatientIdsByCounselorId(counselor.getId());
        List<String> emptyStatusPatientIds = statusService.getAllEmptyStatusPatientIds();
        List<String> allPatientIds = new ArrayList<>();
        allPatientIds.addAll(emptyStatusPatientIds);
        allPatientIds.addAll(appointmentPatientIds);

        return (List<Account>) accountRepository.findAllById(allPatientIds);
    }

    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(id);
    }

    public List<UserData> findAllAccounts(){
        List<Account> accountList = accountRepository.findAllAccounts();
        List<UserData> userDataList = new ArrayList<>();
        for(Account account : accountList){
            userDataList.add(UserData.builder()
                    .role(account.getRole())
                    .id(account.getId())
                    .registrationNumber(account.getRegistrationNumber())
                    .lastName(account.getLastName())
                    .firstName(account.getFirstName())
                    .email(account.getEmail())
                    .dateOfBirth(account.getDateOfBirth())
                    .address(account.getAddress())
                    .build());
        }
        return userDataList;
    }

    public List<Account> getPatientsForDoctor(Account doctor) {
        appointmentService.completeDoctorAppointment(doctor.getId());
        List<String> assignedPatientIds = assignmentService.getAllPatientIdsByDoctorId(doctor.getId());
        return (List<Account>) accountRepository.findAllById(assignedPatientIds);
    }

    public Account getAccountByEmail(String email){
        return accountRepository.findByEmail(email);
    }

    public List<Account> getAllDoctors(){
        return accountRepository.findAccountByRole(Role.DOCTOR);
    }

    public Account edit(EditDto editDto){
        CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Account> account = accountRepository.findById(user.getAccountId());
        if(account.isEmpty()){
            throw new IllegalArgumentException("Login expired, please login first.");
        }
        account.ifPresent(a -> {
            if (!editDto.getPassword().isEmpty() && editDto.getPassword()!=null){
                a.setPassword(bCryptPasswordEncoder.encode(editDto.getPassword()));
            }
            if (!editDto.getAddress().isEmpty() && editDto.getAddress()!=null){
                a.setAddress(editDto.getAddress());
            }
            if (!editDto.getPhoneNumber().isEmpty() && editDto.getPhoneNumber()!=null){
                a.setPhoneNumber(editDto.getPhoneNumber());
            }
        });
        accountRepository.save(account.get());
        return account.get();
    }
}
