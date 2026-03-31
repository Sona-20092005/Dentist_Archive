package com.dentistarchive.exception;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonTypeName("excel-file-validation-error-response")
public class ExcelFileValidationErrorResponse extends ErrorResponse {

    @Builder.Default
    List<ExcelFileValidationError> validationErrors = new ArrayList<>();
}
