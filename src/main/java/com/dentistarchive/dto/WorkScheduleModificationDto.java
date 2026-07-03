package com.dentistarchive.dto;

import com.dentistarchive.enums.ModificationType;
import com.dentistarchive.enums.WorkSessionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class WorkScheduleModificationDto extends ArchivingBaseDto {

    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    ModificationType modificationType;

    WorkSessionType workSessionType;

    LocalDate sourceDate;

    LocalTime sourceStartTime;

    LocalTime sourceEndTime;

    UUID scheduleId;
}


