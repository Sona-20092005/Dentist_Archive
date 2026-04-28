package com.dentistarchive.exception;

import com.dentistarchive.utils.ValidationErrorUtils;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.security.auth.login.CredentialException;
import java.util.Map;

import static com.dentistarchive.exception.ErrorCode.*;

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

    @ExceptionHandler
    public ResponseEntity<ValidationErrorResponse> handle(ConstraintViolationException e, HttpServletRequest request) {
        log.error("Validation error:", e);
        return ResponseEntity
                .badRequest()
                .body(errorResponseCreator.createValidationErrorResponse(
                        e.getConstraintViolations().stream()
                                .map(it -> ValidationError.builder()
                                        .fieldName(ValidationErrorUtils.getFieldName(it.getPropertyPath()))
                                        .fieldPath(ValidationErrorUtils.getFieldPath(it.getPropertyPath()))
                                        .invalidValue(it.getInvalidValue())
                                        .errorCode(it.getMessageTemplate().substring(1, it.getMessageTemplate().length() - 1))
                                        .errorMessage(it.getMessage())
                                        .build())
                                .toList())
                );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(CommonErrorCodeException e, HttpServletRequest request) {
        log.error("Error code exception:", e);
        return ResponseEntity
                .badRequest()
                .body(errorResponseCreator.createErrorResponse(
                        e.getErrorCode(),
                        e.getMessage()
                ));
    }

    @ExceptionHandler
    public ResponseEntity<ValidationErrorResponse> handle(CommonValidationException e, HttpServletRequest request) {
        log.error("Validation error:", e);
        return ResponseEntity
                .badRequest()
                .body(errorResponseCreator.createValidationErrorResponse(e.getValidationErrors()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(AccessDeniedException e, HttpServletRequest request) {
        log.error("Forbidden error:", e);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponseCreator.createErrorResponse(ACCESS_DENIED.getCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(CredentialException e, HttpServletRequest request) {
        log.error("Unauthorized error:", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponseCreator.createErrorResponse(AUTHENTICATION_FAILED.getCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("Http message parsing error:", e);
        return ResponseEntity
                .badRequest()
                .body(errorResponseCreator.createErrorResponse(
                        HTTP_REQUEST_PARSING_ERROR.getCode(),
                        e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        log.error("Http media type of not supported for {} {}:", request.getMethod(), request.getRequestURI(), e);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponseCreator.createErrorResponse(
                        INCORRECT_MEDIA_TYPE_ERROR.getCode(),
                        e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("Http parameters parsing error:", e);
        return ResponseEntity
                .badRequest()
                .body(errorResponseCreator.createErrorResponse(
                        HTTP_REQUEST_PARSING_ERROR.getCode(),
                        e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("Method argument validation error:", e);
        return ResponseEntity
                .badRequest()
                .body(errorResponseCreator.createErrorResponse(VALIDATION_ERROR.getCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MismatchedInputException e, HttpServletRequest request) {
        log.error("Mismatched input error:", e);
        return ResponseEntity
                .badRequest()
                .body(errorResponseCreator.createErrorResponse(VALIDATION_ERROR.getCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(MissingRequestHeaderException e, HttpServletRequest request) {
        log.error("Missing request header error:", e);
        return ResponseEntity
                .badRequest()
                .body(errorResponseCreator.createErrorResponse(VALIDATION_ERROR.getCode(), e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(Throwable e, HttpServletRequest request) {
        log.error("Unexpected error:", e);
        return ResponseEntity
                .internalServerError()
                .body(errorResponseCreator.createErrorResponse(SERVER_ERROR.getCode(), e.getMessage()));
    }

}
