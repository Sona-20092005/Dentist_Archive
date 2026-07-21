package com.dentistarchive.dto;

import com.dentistarchive.enums.AppointmentSchedulingStatus;
import com.dentistarchive.enums.AppointmentStatus;
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
public class AppointmentDto extends ArchivingBaseDto {

    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    AppointmentStatus appointmentStatus;

    AppointmentSchedulingStatus appointmentSchedulingStatus;

    String notes;

    UUID nurseId;

    UUID patientId;

    UUID scheduleId;
}


