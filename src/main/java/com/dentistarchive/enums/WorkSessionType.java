package com.dentistarchive.enums;

import lombok.Getter;

@Getter
public enum WorkSessionType {
    REGULAR(10),
    OVERTIME(20);

    private final int sortOrder;

    WorkSessionType(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
