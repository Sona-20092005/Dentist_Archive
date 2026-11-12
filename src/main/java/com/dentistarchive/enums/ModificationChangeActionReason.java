package com.dentistarchive.enums;

import lombok.Getter;

@Getter
public enum ModificationChangeActionReason {
    SOURCE_SESSION_REMOVED,
    TARGET_SESSION_CONFLICT,
    MODIFICATION_REDUNDANT,
    SOURCE_SESSION_STILL_EXISTS
}
