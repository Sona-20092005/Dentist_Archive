package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.MutableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
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

    @NotNull
    @Column(name = "tooth_number", nullable = false)
    Integer toothNumber;

    @NotBlank
    String description;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    UUID patientId;
}


