package com.dentistarchive.dto.update;

import com.dentistarchive.dto.WorkScheduleRuleDto;
import com.dentistarchive.dto.create.BaseCreateDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class WorkScheduleRuleCorrectDto extends BaseCreateDto<WorkScheduleRuleDto> {

    DayOfWeek dayOfWeek;

    LocalTime startTime;

    LocalTime endTime;

    String notes;
}
