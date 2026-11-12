package com.dentistarchive.entity.material;

import com.dentistarchive.entity.ArchivableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialPurchase extends ArchivableBaseEntity {

    @NotNull
    @Column(name = "material_supplier_id", nullable = false)
    UUID materialSupplierId;

    @NonNull
    @Positive
    @Column(nullable = false)
    Integer quantity;

    @NotNull
    @PositiveOrZero
    @Column(name = "unit_price", nullable = false)
    BigDecimal unitPrice;

    @NotNull
    @PositiveOrZero
    @Column(name = "total_Amount", nullable = false)
    BigDecimal totalAmount;

    @NotNull
    @Column(name = "purchase_date", nullable = false)
    LocalDate purchaseDate;

    String notes;
}
