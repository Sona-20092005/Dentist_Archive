package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.MutableBaseEntity;
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
public class ToothConditionNote extends MutableBaseEntity {

    @NonNull
    @Column(name = "tooth_number", nullable = false)
    Integer toothNumber;

    @Column(nullable = false)
    String description;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    UUID patientId;
}


