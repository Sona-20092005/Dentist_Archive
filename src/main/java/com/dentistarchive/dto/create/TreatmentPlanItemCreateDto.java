package com.dentistarchive.dto.create;

import com.dentistarchive.dto.TreatmentPlanItemDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class TreatmentPlanItemCreateDto extends BaseCreateDto<TreatmentPlanItemDto>{

    @NotNull
    @PositiveOrZero
    BigDecimal unitPrice;

    @NotNull
    @Positive
    Integer quantity;

    List<@NotNull Integer> toothNumbers;

    String notes;

    @NotNull
    UUID procedureId;

    @NotNull
    UUID treatmentPlanId;

}
