package com.dentistarchive.entity.material;

import com.dentistarchive.entity.ArchivableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialSupplier extends ArchivableBaseEntity {

    @NotNull
    @Column(name = "unit_price", nullable = false)
    BigDecimal unitPrice;

    @NotNull
    @Column(name = "last_price_update")
    OffsetDateTime lastPriceUpdate;

    @Column(name = "unit_of_measure")
    String unitOfMeasure;

    String productName;

    String notes;

    @NotNull
    @Column(name = "material_id", nullable = false)
    UUID materialId;

    @NotNull
    @Column(name = "supplier_id", nullable = false)
    UUID supplierId;
}
