package com.dentistarchive.exception;

import java.util.Objects;

public abstract class CommonErrorCodeException extends RuntimeException {

    public abstract String getErrorCode();

    @Override
    public String getMessage() {
        return getErrorCode();
    }

    public boolean hasOverriddenMessage() {
        return !Objects.equals(getMessage(), getErrorCode());
    }
}
