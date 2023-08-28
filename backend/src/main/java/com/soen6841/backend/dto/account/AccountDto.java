package com.soen6841.backend.dto.account;

import com.soen6841.backend.constant.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AccountDto {

    private String id;
    private String email;
    private String firstName;
    private String LastName;
    private String address;
    private String phoneNumber;
    private Role role;
    private Date dateOfBirth;
    private String registrationNumber;
}
