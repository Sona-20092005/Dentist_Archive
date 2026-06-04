package com.dentistarchive.entity.pricelist;

import com.dentistarchive.entity.ArchivableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Procedure extends ArchivableBaseEntity {

    @NotNull
    @Column(nullable = false)
    String name;

    @NotNull
    @Column(nullable = false)
    BigDecimal price;

    String description;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;
}


