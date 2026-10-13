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
public class Material extends ArchivableBaseEntity {

    @NotNull
    @Column(nullable = false)
    String name;

    String description;

    String notes;

    @NotNull
    @Column(name = "category_id", nullable = false)
    UUID categoryId;
}
