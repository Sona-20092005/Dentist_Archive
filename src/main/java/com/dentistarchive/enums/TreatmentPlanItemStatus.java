package com.dentistarchive.enums;

import lombok.Getter;

@Getter
public enum TreatmentPlanItemStatus {
    ACTIVE(10),
    COMPLETED(20),
    CANCELLED(30);

    private final int sortOrder;

    TreatmentPlanItemStatus(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}