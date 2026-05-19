package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.MutableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
//@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tooth extends MutableBaseEntity {

    int number;

    String description;

    @Column(name = "patient_id", nullable = false)
    UUID patientId;
}


