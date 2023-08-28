package com.soen6841.backend.constant;

import java.util.Arrays;
import java.util.List;

public enum Role {
    MANAGER(1), PATIENT(2), DOCTOR(3), COUNSELOR(4);

    private int order;
    Role(int order){
        this.order = order;
    }

    public static List<Role> MANAGER_CREATION_ROLE = Arrays.asList(PATIENT, DOCTOR, COUNSELOR);
}
