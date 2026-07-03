package com.dentistarchive.dto.update;

import com.dentistarchive.dto.WorkScheduleDto;
import com.dentistarchive.dto.create.BaseCreateDto;
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
public class WorkScheduleUpdateDto extends BaseCreateDto<WorkScheduleDto> {

    String name;

    String notes;

    LocalDate effectiveFrom;

    LocalDate effectiveUntil;

    UUID clinicId;
}
