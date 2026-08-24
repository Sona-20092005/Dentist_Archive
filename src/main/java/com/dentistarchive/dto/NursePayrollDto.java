package com.dentistarchive.dto;

import com.dentistarchive.enums.CompensationType;
import com.dentistarchive.enums.NursePayrollStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class NursePayrollDto extends ArchivingBaseDto {

    UUID nurseId;

    NursePayrollStatus status;

    Integer year;

    Month month;

    BigDecimal calculatedRegularAmount;

    BigDecimal regularAmount;

    BigDecimal calculatedOvertimeAmount;

    BigDecimal overtimeAmount;

    BigDecimal bonus;

    BigDecimal deduction;

    Integer regularMinutesWorked;

    Integer overtimeMinutesWorked;

    LocalDate paymentDate;

    CompensationType compensationType;

    Boolean isManuallyAdjusted;

    Boolean locked;

    String notes;

}



