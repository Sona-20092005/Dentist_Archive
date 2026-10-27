package com.dentistarchive.dto.update;

import com.dentistarchive.dto.CompletedTreatmentDto;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class CompletedTreatmentUpdateDto extends BaseUpdateDto<CompletedTreatmentDto> {

    @NotNull
    LocalDate date;

    @PositiveOrZero
    BigDecimal unitPrice;

    @NotNull
    @Positive
    Integer quantity;

    List<@NotNull Integer> toothNumbers;

    String notes;

    @NotNull
    UUID procedureId;

    UUID appointmentId;

    UUID treatmentPlanItemId;
}
