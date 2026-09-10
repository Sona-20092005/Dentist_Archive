package com.dentistarchive.dto.update;

import com.dentistarchive.dto.TechnicianMonthlyReportDto;
import com.dentistarchive.enums.TechnicianMonthlyReportStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class TechnicianMonthlyReportUpdateDto extends BaseUpdateDto<TechnicianMonthlyReportDto> {

    TechnicianMonthlyReportStatus status;

    BigDecimal totalAmount;

    BigDecimal paidAmount;

    BigDecimal bonus;

    BigDecimal deduction;

    String notes;

}
