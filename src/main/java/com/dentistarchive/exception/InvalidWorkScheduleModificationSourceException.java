package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidWorkScheduleModificationSourceException extends CommonErrorCodeException {

    @Override
    public String getErrorCode() {
        return ErrorCode.WORK_SCHEDULE_MODIFICATION_SOURCE_INVALID.getCode();
    }

}