package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseCompensationTypeChangeAlreadyExistsException extends CommonErrorCodeException {

    @Override
    public String getErrorCode() {
        return ErrorCode.NURSE_COMPENSATION_TYPE_CHANGE_ALREADY_EXISTS.getCode();
    }

}