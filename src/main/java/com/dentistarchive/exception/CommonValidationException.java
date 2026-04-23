package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonValidationException extends RuntimeException {

    List<ValidationError> validationErrors = new ArrayList<>();

    public CommonValidationException(String field, String errorCode, Object... args) {
        addValidationError(field, errorCode, args);
    }

    public CommonValidationException(String errorCode, Object... args) {
        addValidationError(errorCode, args);
    }

    public CommonValidationException addValidationError(String field, String errorCode, Object... args) {
        validationErrors.add(ValidationError.builder()
                .fieldName(field)
                .errorCode(errorCode) //think about errorMessage
                .build());
        return this;
    }

    public CommonValidationException addValidationError(String errorCode, Object... args) {
        return addValidationError(null, errorCode, args);
    }

    @Override
    public String getMessage() {
        return validationErrors.stream().map(ValidationError::getErrorMessage).collect(joining("\n"));
    }
}
