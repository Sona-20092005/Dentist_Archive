package com.dentistarchive.enums;

import lombok.Getter;

@Getter
public enum AppointmentStatus {
    SCHEDULED(10),
    COMPLETED(20),
    CANCELLED(30),
    NO_SHOW(40);

    private final int sortOrder;

    AppointmentStatus(int sortOrder) {
        this.sortOrder = sortOrder;
    }

}
