package com.dentistarchive.dto.update;

import com.dentistarchive.dto.TechnicianMonthlyReportDto;
import com.dentistarchive.enums.TechnicianMonthlyReportStatus;
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

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class TechnicianMonthlyReportUpdateDto extends BaseUpdateDto<TechnicianMonthlyReportDto> {

    @NotNull
    TechnicianMonthlyReportStatus status;

    @NotNull
    @PositiveOrZero
    BigDecimal totalAmount;

    @NotNull
    @PositiveOrZero
    BigDecimal paidAmount;

    @NotNull
    @PositiveOrZero
    BigDecimal bonus;

    @NotNull
    @PositiveOrZero
    BigDecimal deduction;

    String notes;

}
