package com.dentistarchive.exception;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EntityNotArchivedException extends CommonErrorCodeException {

    UUID id;
    Class<?> entityClass;

    @Override
    public String getErrorCode() {
        return ErrorCode.ENTITY_NOT_ARCHIVED.getCode();
    }

    @Override
    public String getMessage() {
        return String.format(
                "%s with id '%s' is not archived",
                entityClass.getSimpleName(),
                id
        );
    }

}
