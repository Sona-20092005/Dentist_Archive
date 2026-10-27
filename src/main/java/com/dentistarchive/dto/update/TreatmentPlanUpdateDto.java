package com.dentistarchive.dto.update;

import com.dentistarchive.dto.TreatmentPlanDto;
import com.dentistarchive.enums.TreatmentPlanStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
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
public class TreatmentPlanUpdateDto extends BaseUpdateDto<TreatmentPlanDto> {

    @NotNull
    LocalDate date;

    @NotNull
    TreatmentPlanStatus planStatus;

    @DecimalMin("0.00")
    @DecimalMax("100.00")
    BigDecimal discountPercent;

    @PositiveOrZero
    BigDecimal discountAmount;

    @NotNull
    @PositiveOrZero
    BigDecimal finalAmount;

    @NotNull
    String notes;
}
