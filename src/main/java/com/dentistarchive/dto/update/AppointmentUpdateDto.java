package com.dentistarchive.dto.update;

import com.dentistarchive.dto.AppointmentDto;
import com.dentistarchive.dto.create.BaseCreateDto;
import com.dentistarchive.enums.AppointmentSchedulingStatus;
import com.dentistarchive.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class AppointmentUpdateDto extends BaseCreateDto<AppointmentDto> {

    @NotNull
    LocalDate date;

    @NotNull
    LocalTime startTime;

    @NotNull
    LocalTime endTime;

    @NotNull
    AppointmentStatus appointmentStatus;

    @NotNull
    AppointmentSchedulingStatus appointmentSchedulingStatus;

    String notes;

    UUID nurseId;

    @NotNull
    UUID patientId;

    @NotNull
    UUID scheduleId;
}
