package com.dentistarchive.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomValidationException extends CommonValidationException {

    public CustomValidationException(String field, ErrorCode errorCode, Object... args) {
        addValidationError(field, errorCode, args);
    }

    public CustomValidationException(ErrorCode errorCode, Object... args) {
        addValidationError(errorCode, args);
    }

    public CustomValidationException addValidationError(String field, ErrorCode errorCode, Object... args) {
        return (CustomValidationException) addValidationError(field, errorCode.getCode(), args);
    }

    public CustomValidationException addValidationError(ErrorCode errorCode, Object... args) {
        return (CustomValidationException) addValidationError(null, errorCode.getCode(), args);
    }
}
