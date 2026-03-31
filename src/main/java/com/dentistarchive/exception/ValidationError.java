package com.dentistarchive.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationError {

    String fieldName;

    String fieldPath;

    Object invalidValue;

    String errorCode;

    String errorMessage;
}
