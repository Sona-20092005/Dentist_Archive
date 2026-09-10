package com.dentistarchive.dto;

import com.dentistarchive.enums.TechnicianMonthlyReportStatus;
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
public class TechnicianMonthlyReportDto extends MutableBaseDto {

    UUID technicianId;

    TechnicianMonthlyReportStatus status;

    YearMonth yearMonth;

    BigDecimal totalAmount;

    BigDecimal paidAmount;

    BigDecimal bonus;

    BigDecimal deduction;

    Integer numberOfWorkLogs;

    Boolean locked;

    String notes;

}




