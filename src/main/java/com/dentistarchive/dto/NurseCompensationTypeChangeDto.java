package com.dentistarchive.dto;

import com.dentistarchive.enums.CompensationType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.YearMonth;
import java.util.UUID;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class NurseCompensationTypeChangeDto extends MutableBaseDto {

    UUID nurseId;

    CompensationType compensationType;

    YearMonth effectiveFrom;
}



