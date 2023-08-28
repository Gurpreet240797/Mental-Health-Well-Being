package com.soen6841.backend.dto;

import com.soen6841.backend.constant.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserData {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String registrationNumber;
    private Date dateOfBirth;
    private String address;

}
