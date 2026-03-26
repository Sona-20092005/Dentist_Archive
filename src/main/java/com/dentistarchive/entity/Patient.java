package com.dentistarchive.entity;

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
public class Patient extends ArchivingBaseEntity {

    @NotNull
    String name;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> phones;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> emails;

    String address;

    @Column(name = "passport_information")
    String passportInformation;

    String notes;

}


