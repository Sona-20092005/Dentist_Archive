package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManyFailedLoginAttemptsException extends RuntimeException {

    Duration durationTillNextLoginAttempt;

    public String getErrorCode() {
        return ErrorCode.LOGIN_DELAY_AFTER_FAILED_ATTEMPTS.getCode();
    }

    @Override
    public String getMessage() {
        long seconds = Math.max(durationTillNextLoginAttempt.toSeconds(), 1);
        return "Too many failed login attempts. Try again in " + seconds + " seconds.";
    }
}
