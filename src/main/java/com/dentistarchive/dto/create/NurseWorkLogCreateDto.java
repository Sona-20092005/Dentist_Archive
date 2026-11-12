package com.dentistarchive.dto.create;

import com.dentistarchive.dto.NurseWorkLogDto;
import com.dentistarchive.enums.NurseWorkType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class NurseWorkLogCreateDto extends BaseCreateDto<NurseWorkLogDto>{

    @NotNull
    UUID nurseId;

    @NotNull
    NurseWorkType workType;

    @NotNull
    LocalDate date;

    @NotNull
    LocalTime startTime;

    @NotNull
    LocalTime endTime;

    @PositiveOrZero
    BigDecimal hourlyRate;

    String notes;
}
