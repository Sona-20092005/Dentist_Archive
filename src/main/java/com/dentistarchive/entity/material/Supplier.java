package com.dentistarchive.entity.material;

import com.dentistarchive.entity.ArchivableBaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Supplier extends ArchivableBaseEntity {

    @NotNull
    @Column(nullable = false)
    String name;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> phones;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> emails;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> addresses;

    String website;

    String notes;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;
}


