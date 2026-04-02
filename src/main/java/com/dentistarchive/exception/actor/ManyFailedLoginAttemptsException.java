package com.dentistarchive.exception.actor;

import com.skapps.tech.common.locale.utils.MessageSourceUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.util.List;

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
        return MessageSourceUtils.getMessageOrElseCode(
                getErrorCode(),
                List.of(Math.max(durationTillNextLoginAttempt.toSeconds(), 1))
        );
    }
}
