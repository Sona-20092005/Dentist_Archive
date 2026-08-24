package com.dentistarchive.dto.update;

import com.dentistarchive.dto.NursePayrollDto;
import com.dentistarchive.enums.NursePayrollStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class NursePayrollUpdateDto extends BaseUpdateDto<NursePayrollDto> {

    NursePayrollStatus status;

    BigDecimal regularAmount;

    BigDecimal overtimeAmount;

    BigDecimal bonus;

    BigDecimal deduction;

    LocalDate paymentDate;

    String notes;

}
