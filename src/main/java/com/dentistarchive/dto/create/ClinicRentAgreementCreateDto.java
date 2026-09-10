package com.dentistarchive.dto.create;

import com.dentistarchive.dto.ClinicRentAgreementDto;
import com.dentistarchive.enums.RentCalculationType;
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
public class ClinicRentAgreementCreateDto extends BaseCreateDto<ClinicRentAgreementDto> {

    UUID clinicId;

    YearMonth effectiveFrom;

    YearMonth effectiveUntil;

    RentCalculationType rentCalculationType;

    BigDecimal fixedMonthlyRent;

    BigDecimal hourlyRate;

    BigDecimal overtimeHourlyRate;

    Integer collectionPercentage;

    String notes;

}
