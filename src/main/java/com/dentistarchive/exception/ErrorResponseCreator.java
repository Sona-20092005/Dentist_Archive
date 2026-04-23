package com.dentistarchive.exception;

import com.dentistarchive.utils.ClockUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dentistarchive.exception.ErrorCode.VALIDATION_ERROR;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponseCreator {

    @Value("${spring.application.name}")
    String applicationName;

    public ErrorResponse createErrorResponse(String errorCode, String errorMessage) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .dateTime(ClockUtils.now())
                .appName(applicationName)
                .build();
    }

    public ValidationErrorResponse createValidationErrorResponse(List<ValidationError> validationErrors) {
        return ValidationErrorResponse.builder()
                .errorCode(VALIDATION_ERROR.getCode())
                .validationErrors(validationErrors)
                .dateTime(ClockUtils.now())
                .appName(applicationName)
                .build();
    }

}
