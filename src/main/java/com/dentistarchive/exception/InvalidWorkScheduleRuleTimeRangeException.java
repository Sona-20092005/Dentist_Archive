package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidWorkScheduleRuleTimeRangeException extends CommonErrorCodeException {

    @Override
    public String getErrorCode() {
        return ErrorCode.WORK_SCHEDULE_RULE_TIME_RANGE_INVALID.getCode();
    }

}