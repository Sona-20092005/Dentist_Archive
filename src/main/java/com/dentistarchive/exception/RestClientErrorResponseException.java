package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Exception for errors during inter service communication
 */
@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestClientErrorResponseException extends RuntimeException {

    int httpStatusCode;

    ErrorResponse errorResponse;

    @Override
    public String getMessage() {
        String message = "http status: " + httpStatusCode + ", error code: ";
        if (errorResponse != null && errorResponse.getErrorCode() != null) {
            return message + errorResponse.getErrorCode();
        }
        return message + "null";
    }
}
