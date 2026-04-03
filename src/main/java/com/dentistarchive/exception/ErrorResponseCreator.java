package com.dentistarchive.exception;

import com.dentistarchive.utils.ClockUtils;
import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.dentistarchive.exception.ErrorResponse.CommonErrorCodes.EXCEL_FILE_VALIDATION_ERROR;
import static com.dentistarchive.exception.ErrorResponse.CommonErrorCodes.VALIDATION_ERROR;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponseCreator {

    ObjectProvider<Tracer> tracerProvider;

    @Value("${spring.application.name}")
    String applicationName;

    public ErrorResponse createErrorResponse(String errorCode, String errorMessage) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .dateTime(ClockUtils.now())
                .appName(applicationName)
                .traceId(getTraceId())
                .build();
    }

    public ValidationErrorResponse createValidationErrorResponse(List<ValidationError> validationErrors) {
        return ValidationErrorResponse.builder()
                .errorCode(VALIDATION_ERROR)
                .validationErrors(validationErrors)
                .dateTime(ClockUtils.now())
                .appName(applicationName)
                .traceId(getTraceId())
                .build();
    }

    public ExcelFileValidationErrorResponse createExcelFileValidationErrorResponse(List<ExcelFileValidationError> validationErrors) {
        return ExcelFileValidationErrorResponse.builder()
                .errorCode(EXCEL_FILE_VALIDATION_ERROR)
                .validationErrors(validationErrors)
                .dateTime(ClockUtils.now())
                .appName(applicationName)
                .traceId(getTraceId())
                .build();
    }

    private String getTraceId() {
        Tracer tracer = tracerProvider.getIfAvailable();
        if (tracer == null) {
            return null;
        }

        CurrentTraceContext currentTraceContext = tracer.currentTraceContext();
        if (currentTraceContext == null) {
            return null;
        }

        TraceContext context = currentTraceContext.context();
        if (context == null) {
            return null;
        }

        return context.traceId();
    }
}
