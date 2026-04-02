package com.dentistarchive.exception.actor;


import com.dentistarchive.exception.CommonErrorCodeException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

// TODO: 4/2/2026 remove locale 
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordIsNotSecureException extends CommonErrorCodeException {

    boolean clientPassword;

    @Override
    public String getErrorCode() {
        return clientPassword
                ? ErrorCode.CLIENT_PASSWORD_IS_NOT_SECURE.getCode()
                : ErrorCode.USER_PASSWORD_IS_NOT_SECURE.getCode();
    }

//    @Override
//    public String getMessage() {
//        return MessageSourceUtils.getMessageOrElseCode(getErrorCode());
//    }
}
