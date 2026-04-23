package com.dentistarchive.exception;

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

import static com.dentistarchive.exception.ErrorCode.SERVER_ERROR;
import static com.dentistarchive.exception.ErrorCode.VALIDATION_ERROR;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {

    ErrorResponseCreator errorResponseCreator;

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handle(DataIntegrityViolationException e) {
        log.error("Database exception:", e);
        if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException exception) {
            return ResponseEntity
                    .badRequest()
                    .body(errorResponseCreator.createErrorResponse(
                            VALIDATION_ERROR.getCode(),
                            "database.constraint." + exception.getConstraintName()
                    ));
        }
        return ResponseEntity
                .internalServerError()
                .body(errorResponseCreator.createErrorResponse(SERVER_ERROR.getCode(), e.toString()));
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

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handle(PasswordIsNotSecureException e) {
        log.error("Password is not secure:", e);
        return ResponseEntity
                .badRequest()
                .body(errorResponseCreator.createErrorResponse(
                        e.getErrorCode(),
                        e.getMessage()
                ));
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handle(EntityNotFoundByIdException e) {
        log.error("Entity not found", e);
        return ResponseEntity
                .badRequest()
                .body(errorResponseCreator.createErrorResponse(
                        e.getErrorCode(),
                        e.getMessage()
                ));
    }

}
