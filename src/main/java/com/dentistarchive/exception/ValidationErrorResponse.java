package com.dentistarchive.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@JsonTypeName("validation-error-response")
public class ValidationErrorResponse extends ErrorResponse {

    @Builder.Default
    List<ValidationError> validationErrors = new ArrayList<>();

    /**
     * Some jakarta validations can partially duplicate others
     * e.g. @NotBlank @Size(min = 3) @Pattern("^[0-9]+$") will throw 3 errors for empty string
     * This setting created to avoid errors duplicates for the same field
     */
    @Builder.Default
    @JsonIgnore
    boolean returnOnlyOneErrorPerField = true;

    public List<ValidationError> getValidationErrors() {
        if (returnOnlyOneErrorPerField) {
            List<ValidationError> validationErrorsWithoutDuplicates = new ArrayList<>();
            validationErrors.forEach(validationError -> {
                if (validationError.getFieldPath() != null) {
                    boolean notExists = validationErrorsWithoutDuplicates.stream()
                            .noneMatch(it -> validationError.getFieldPath().equals(it.getFieldPath()));
                    if (notExists) {
                        validationErrorsWithoutDuplicates.add(validationError);
                    }
                } else if (validationError.getFieldName() != null) {
                    boolean notExists = validationErrorsWithoutDuplicates.stream()
                            .noneMatch(it -> it.getFieldPath() == null && validationError.getFieldName().equals(it.getFieldName()));
                    if (notExists) {
                        validationErrorsWithoutDuplicates.add(validationError);
                    }
                } else {
                    validationErrorsWithoutDuplicates.add(validationError);
                }
            });
            return validationErrorsWithoutDuplicates;
        }
        return validationErrors;
    }
}
