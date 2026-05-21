package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.MutableBaseEntity;
import jakarta.persistence.Column;
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
public class ToothConditionNote extends MutableBaseEntity {

    int number;

    String description;

    @Column(name = "patient_id", nullable = false)
    UUID patientId;
}


