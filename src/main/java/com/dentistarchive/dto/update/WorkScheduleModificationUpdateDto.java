package com.dentistarchive.dto.update;

import com.dentistarchive.dto.WorkScheduleModificationDto;
import com.dentistarchive.dto.create.BaseCreateDto;
import com.dentistarchive.enums.ModificationType;
import com.dentistarchive.enums.WorkSessionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class WorkScheduleModificationUpdateDto extends BaseCreateDto<WorkScheduleModificationDto> {

    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    ModificationType modificationType;

    WorkSessionType workSessionType;

    LocalDate sourceDate;

    LocalTime sourceStartTime;

    LocalTime sourceEndTime;
}
