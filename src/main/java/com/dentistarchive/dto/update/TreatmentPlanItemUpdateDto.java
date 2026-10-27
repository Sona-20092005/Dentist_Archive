package com.dentistarchive.dto.update;

import com.dentistarchive.dto.TreatmentPlanItemDto;
import com.dentistarchive.enums.TreatmentPlanItemStatus;
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
public class TreatmentPlanItemUpdateDto extends BaseUpdateDto<TreatmentPlanItemDto> {

    @NotNull
    TreatmentPlanItemStatus itemStatus;

    @NotNull
    @Positive
    Integer quantity;

    @NotNull
    @PositiveOrZero
    BigDecimal unitPrice;

    List<@NotNull Integer> toothNumbers;

    String notes;

    @NotNull
    UUID procedureId;
}
