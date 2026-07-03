package com.dentistarchive.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class WorkScheduleDto extends ArchivingBaseDto {

    String name;

    String notes;

    LocalDate effectiveFrom;

    LocalDate effectiveUntil;

    UUID doctorId;

    UUID clinicId;
}


