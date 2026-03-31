package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CommonErrorCode {
    ENTITIES_NOT_FOUND("validation.entities-not-found"),
    ENTITY_NOT_FOUND("validation.entity-not-found"),
    IMPOSSIBLE_TO_PATCH_ARCHIVED_ENTITY("validation.impossible-to-patch-archived-entity"),
    FILE_MUST_NOT_BE_EMPTY("validation.file-must-not-be-empty");

    String code;
}
