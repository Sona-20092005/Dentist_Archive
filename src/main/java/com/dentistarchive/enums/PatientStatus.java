package com.dentistarchive.enums;

import lombok.Getter;

@Getter
public enum PatientStatus {
    WAITING_FOR_APPOINTMENT(10),
    IN_TREATMENT(20),
    ACTIVE(30),
    NEW(40),
    INACTIVE(50);

    private final int sortOrder;

    PatientStatus(int sortOrder) {
        this.sortOrder = sortOrder;
    }

}
