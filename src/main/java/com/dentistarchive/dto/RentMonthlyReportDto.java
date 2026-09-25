package com.dentistarchive.dto;

import com.dentistarchive.enums.RentMonthlyReportStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class RentMonthlyReportDto extends MutableBaseDto {

    UUID clinicRentAgreementId;

    YearMonth yearMonth;

    BigDecimal regularAmount;

    BigDecimal overtimeAmount;

    Integer regularMinutesUsed;

    Integer overtimeMinutesUsed;

    BigDecimal manualAdjustment;

    RentMonthlyReportStatus status;

    Boolean locked;

    String notes;
}




