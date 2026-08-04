package com.dentistarchive.dto;

import com.dentistarchive.enums.NurseWorkType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class NurseWorkLogDto extends ArchivingBaseDto {

    UUID nurseId;

    NurseWorkType workType;

    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    BigDecimal hourlyRate;

    String notes;

}


