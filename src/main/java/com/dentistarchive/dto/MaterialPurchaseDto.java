package com.dentistarchive.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class MaterialPurchaseDto extends ArchivingBaseDto {

    UUID materialSupplierId;

    Integer quantity;

    BigDecimal unitPrice;

    BigDecimal totalAmount;

    LocalDate purchaseDate;

    String notes;

}
