package com.dentistarchive.exception;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientNotInactiveException extends CommonErrorCodeException {

    UUID id;

    @Override
    public String getErrorCode() {
        return ErrorCode.PATIENT_NOT_INACTIVE.getCode();
    }

    @Override
    public String getMessage() {
        return String.format(
                "Patient with id '%s' is not inactive",
                id
        );
    }

}
