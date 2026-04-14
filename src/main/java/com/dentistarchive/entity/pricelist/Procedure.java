package com.dentistarchive.entity.pricelist;

import com.dentistarchive.entity.ArchivableBaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Procedure extends ArchivableBaseEntity {

    @NotNull
    String name;

    BigDecimal price;

    String description;

    String notes;
}
