package com.dentistarchive.dto.create;

import com.dentistarchive.dto.ProcedureDto;
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
public class WorkScheduleRuleCreateDto extends BaseCreateDto<ProcedureDto>{

    DayOfWeek dayOfWeek;

    LocalTime startTime;

    LocalTime endTime;

    String notes;

    UUID scheduleId;
}
