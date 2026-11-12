package com.dentistarchive.enums;

import lombok.Getter;

@Getter
public enum ModificationChangeAction {
    KEEP,
    ARCHIVE,
    CONVERT_TO_ADD,
    CONVERT_TO_CANCEL,
    REQUIRES_RESOLUTION
}
