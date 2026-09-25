package com.dentistarchive.dto.update;

import com.dentistarchive.dto.RentMonthlyReportDto;
import com.dentistarchive.enums.RentMonthlyReportStatus;
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
public class RentMonthlyReportUpdateDto extends BaseUpdateDto<RentMonthlyReportDto> {

    BigDecimal manualAdjustment;

    RentMonthlyReportStatus status;

    String notes;

}
