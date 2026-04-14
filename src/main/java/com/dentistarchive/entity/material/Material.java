package com.dentistarchive.entity.material;

import com.dentistarchive.entity.ArchivableBaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Material extends ArchivableBaseEntity {

    @NotNull
    String name;

    @NotNull
    @Column(name = "unit_price")
    BigDecimal unitPrice;

    // TODO: 3/30/2026 Maybe have a class here to also store addresses
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> stores;

    String description;

    String notes;
}
