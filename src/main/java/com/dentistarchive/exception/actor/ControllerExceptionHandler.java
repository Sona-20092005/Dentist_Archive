package com.dentistarchive.exception.actor;

import com.skapps.tech.common.error.ErrorResponseCreator;
import com.skapps.tech.common.error.response.ErrorResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

import static com.skapps.tech.common.error.response.ErrorResponse.CommonErrorCodes.SERVER_ERROR;
import static com.skapps.tech.common.error.response.ErrorResponse.CommonErrorCodes.VALIDATION_ERROR;
import static com.skapps.tech.common.locale.utils.MessageSourceUtils.getMessageOrElseCode;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ControllerExceptionHandler {

    ErrorResponseCreator errorResponseCreator;

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handle(DataIntegrityViolationException e) {
        log.error("Database exception:", e);
        if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(errorResponseCreator.createErrorResponse(
                            VALIDATION_ERROR,
                            getMessageOrElseCode("database.constraint." + exception.getConstraintName())
                    ));
        }
        return ResponseEntity
                .internalServerError()
                .body(errorResponseCreator.createErrorResponse(SERVER_ERROR, e.toString()));
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handle(ManyFailedLoginAttemptsException e) {
        log.error("Many failed login attempts exception:", e);
        var errorResponse = errorResponseCreator.createErrorResponse(e.getErrorCode(), e.getMessage());
        errorResponse.setAdditionalParams(Map.of(
                "durationTillNextLoginAttemptInSeconds", e.getDurationTillNextLoginAttempt().toSeconds()
        ));
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }
}
