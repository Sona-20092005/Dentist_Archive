package com.dentistarchive.dto.create;

import com.dentistarchive.dto.WorkScheduleRuleDto;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class WorkScheduleRuleCreateDto extends BaseCreateDto<WorkScheduleRuleDto>{

    @NotNull
    DayOfWeek dayOfWeek;

    @NotNull
    LocalTime startTime;

    @NotNull
    LocalTime endTime;

    String notes;

    @NotNull
    UUID scheduleId;
}
