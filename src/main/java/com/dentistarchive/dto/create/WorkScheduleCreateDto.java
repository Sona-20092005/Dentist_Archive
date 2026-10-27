package com.dentistarchive.dto.create;

import com.dentistarchive.dto.WorkScheduleDto;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class WorkScheduleCreateDto extends BaseCreateDto<WorkScheduleDto>{

    String name;

    String notes;

    @NotNull
    LocalDate effectiveFrom;

    LocalDate effectiveUntil;

    @NotNull
    UUID clinicId;
}
