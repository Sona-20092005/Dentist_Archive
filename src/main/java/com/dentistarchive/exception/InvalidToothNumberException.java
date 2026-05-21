package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidToothNumberException extends CommonErrorCodeException {

    @Override
    public String getErrorCode() {
        return ErrorCode.TOOTH_NUMBER_INVALID.getCode();
    }

}