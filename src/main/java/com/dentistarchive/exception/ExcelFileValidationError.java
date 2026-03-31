package com.dentistarchive.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExcelFileValidationError {

    Integer rowNumber;

    Integer columnNumber;

    String errorCode;

    String errorMessage;
}
