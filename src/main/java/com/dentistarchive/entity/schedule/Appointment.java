package com.dentistarchive.entity.schedule;

import com.dentistarchive.entity.MutableBaseEntity;
import com.dentistarchive.enums.TreatmentPlanItemStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Appointment extends MutableBaseEntity {


    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_status", nullable = false)
    TreatmentPlanItemStatus itemStatus;


    @NonNull
    @Column(name = "tooth_number", nullable = false)
    Integer toothNumber;

    @Column(nullable = false)
    String description;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    UUID patientId;
}

//+appointment_id
//+appointment_date
//+start_time
//+end_time
//+status (SCHEDULED, CANCELED)
//+notes

