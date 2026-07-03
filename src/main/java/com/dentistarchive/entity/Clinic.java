package com.dentistarchive.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
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
public class Clinic extends ArchivableBaseEntity {

    @NotBlank
    @Column(nullable = false)
    String name;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> phones;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> emails;

    String address;

    String notes;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;

}


