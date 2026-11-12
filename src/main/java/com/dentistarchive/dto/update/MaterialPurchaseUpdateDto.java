package com.dentistarchive.dto.update;

import com.dentistarchive.dto.MaterialPurchaseDto;
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
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class MaterialPurchaseUpdateDto extends BaseUpdateDto<MaterialPurchaseDto> {

    @NotNull
    UUID materialSupplierId;

    @NotNull
    @Positive
    Integer quantity;

    @NotNull
    @PositiveOrZero
    BigDecimal unitPrice;

    @NotNull
    @PositiveOrZero
    BigDecimal totalAmount;

    @NotNull
    LocalDate purchaseDate;

    String notes;
}
