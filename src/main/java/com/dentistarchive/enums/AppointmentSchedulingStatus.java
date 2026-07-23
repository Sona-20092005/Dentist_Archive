package com.dentistarchive.enums;

import lombok.Getter;

@Getter
public enum AppointmentSchedulingStatus {
    NEEDS_RESCHEDULING(10),
    NORMAL(20);

    private final int sortOrder;

    AppointmentSchedulingStatus(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
