package com.dentistarchive.entity.technician;

import com.dentistarchive.entity.ArchivableBaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Technician extends ArchivableBaseEntity {

    @NotNull
    String name;

    String address;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> phones;

    String notes;

}



