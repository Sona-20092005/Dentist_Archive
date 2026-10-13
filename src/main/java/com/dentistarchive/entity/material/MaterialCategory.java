package com.dentistarchive.entity.material;

import com.dentistarchive.entity.ArchivableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MaterialCategory extends ArchivableBaseEntity {

    @NotNull
    @Column(nullable = false)
    String name;

    String description;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;
}


