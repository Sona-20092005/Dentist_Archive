package com.dentistarchive.dto.update;

import com.dentistarchive.dto.NurseDto;
import com.dentistarchive.dto.create.BaseCreateDto;
import com.dentistarchive.enums.RentCalculationType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
import java.time.YearMonth;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class ClinicRentAgreementUpdateDto extends BaseCreateDto<NurseDto> {

    @NotNull
    YearMonth effectiveFrom;

    YearMonth effectiveUntil;

    @NotNull
    RentCalculationType rentCalculationType;

    @PositiveOrZero
    BigDecimal fixedMonthlyRent;

    @PositiveOrZero
    BigDecimal hourlyRate;

    @PositiveOrZero
    BigDecimal overtimeHourlyRate;

    @DecimalMin("0.00")
    @DecimalMax("100.00")
    Integer collectionPercentage;

    String notes;
}
