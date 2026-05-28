package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ToothNoteAlreadyExistsException extends CommonErrorCodeException {

    @Override
    public String getErrorCode() {
        return ErrorCode.TOOTH_NOTE_ALREADY_EXISTS.getCode();
    }

}