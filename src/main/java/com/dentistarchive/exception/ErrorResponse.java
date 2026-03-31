package com.dentistarchive.exception;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ValidationErrorResponse.class),
        @JsonSubTypes.Type(value = ExcelFileValidationErrorResponse.class)
})
@JsonTypeName("error-response")
public class ErrorResponse {

    String errorCode;

    String errorMessage;

    OffsetDateTime dateTime;

    String appName;

    String traceId;

    Map<String, Object> additionalParams;

    public interface CommonErrorCodes {

        String SERVER_ERROR = "server.error";
        String VALIDATION_ERROR = "validation.error";
        String INCORRECT_MEDIA_TYPE_ERROR = "incorrect.content.type.error";
        String EXCEL_FILE_VALIDATION_ERROR = "excel.file.validation.error";
        String HTTP_REQUEST_PARSING_ERROR = "validation.http.request.format.error";
        String ACCESS_DENIED = "access.denied";
        String AUTHENTICATION_FAILED = "authentication.failed";
    }
}
