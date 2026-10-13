package com.dentistarchive.dto.create;

import com.dentistarchive.dto.MaterialSupplierDto;
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
public class MaterialSupplierCreateDto extends BaseCreateDto<MaterialSupplierDto>{

    BigDecimal unitPrice;

    OffsetDateTime lastPriceUpdate;

    String unitOfMeasure;

    String productName;

    String notes;

    UUID materialId;

    UUID supplierId;
}
