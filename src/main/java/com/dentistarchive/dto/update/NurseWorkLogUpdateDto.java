package com.dentistarchive.dto.update;

import com.dentistarchive.dto.NurseWorkLogDto;
import com.dentistarchive.enums.NurseWorkType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class NurseWorkLogUpdateDto extends BaseUpdateDto<NurseWorkLogDto> {

    NurseWorkType workType;

    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    BigDecimal hourlyRate;

    String notes;
}
