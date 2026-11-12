package com.dentistarchive.dto.update;

import com.dentistarchive.dto.MaterialSupplierDto;
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
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class MaterialSupplierUpdateDto extends BaseUpdateDto<MaterialSupplierDto> {

    @NotNull
    @PositiveOrZero
    BigDecimal unitPrice;

    @NotNull
    OffsetDateTime lastPriceUpdate;

    String unitOfMeasure;

    String productName;

    String notes;

    @NotNull
    UUID materialId;

    @NotNull
    UUID supplierId;
}
