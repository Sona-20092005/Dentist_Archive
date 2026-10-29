package com.dentistarchive.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class WorkSchedulePeriodChangePreviewDto {
    boolean confirmationRequired;
    List<WorkScheduleModificationImpactDto> affectedModifications;
}