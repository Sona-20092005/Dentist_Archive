package com.dentistarchive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
@FieldNameConstants
public abstract class BaseDto {

    @Schema(description = "Id")
    UUID id;

    @Schema(description = "Creation date and time")
    OffsetDateTime createdAt;

    @Schema(description = "Id of actor who created entity")
    UUID createdBy;
}
