package com.dentistarchive.dto.create;

import com.dentistarchive.dto.ProcedureDto;
import com.dentistarchive.enums.AppointmentStatus;
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
public class AppointmentCreateDto extends BaseCreateDto<ProcedureDto>{

    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    AppointmentStatus appointmentStatus;

    String notes;

    UUID nurseId;

    UUID patientId;
}
