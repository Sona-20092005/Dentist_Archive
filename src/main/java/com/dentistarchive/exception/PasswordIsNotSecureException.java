package com.dentistarchive.exception;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordIsNotSecureException extends CommonErrorCodeException {

    @Override
    public String getErrorCode() {
        return ErrorCode.USER_PASSWORD_IS_NOT_SECURE.getCode();
    }

}
