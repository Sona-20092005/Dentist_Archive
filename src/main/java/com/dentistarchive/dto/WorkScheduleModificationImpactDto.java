package com.dentistarchive.dto;

import com.dentistarchive.enums.ModificationPeriodChangeAction;
import com.dentistarchive.enums.ModificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class WorkScheduleModificationImpactDto {
    UUID modificationId;
    ModificationType currentType;
    ModificationPeriodChangeAction action;
    LocalDate sourceDate;
    LocalDate targetDate;
}