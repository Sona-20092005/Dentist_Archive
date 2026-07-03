package com.dentistarchive.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class WorkScheduleRuleDto extends ArchivingBaseDto {

    DayOfWeek dayOfWeek;

    LocalTime startTime;

    LocalTime endTime;

    String notes;

    UUID scheduleId;
}


