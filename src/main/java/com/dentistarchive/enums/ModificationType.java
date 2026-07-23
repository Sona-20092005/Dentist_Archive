package com.dentistarchive.enums;

import lombok.Getter;

@Getter
public enum ModificationType {
    ADD(10),
    MODIFY(20),
    CANCEL(30);

    private final int sortOrder;

    ModificationType(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
