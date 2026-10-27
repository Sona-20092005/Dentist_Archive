package com.dentistarchive.dto.create;

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
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class TreatmentPlanCreateDto extends BaseCreateDto<TreatmentPlanDto>{

    @NotNull
    LocalDate date;

    @NotNull
    TreatmentPlanStatus planStatus;

    @DecimalMin("0.00")
    @DecimalMax("100.00")
    BigDecimal discountPercent;

    @PositiveOrZero
    BigDecimal discountAmount;

    String notes;

    @NotNull
    UUID patientId;

}
