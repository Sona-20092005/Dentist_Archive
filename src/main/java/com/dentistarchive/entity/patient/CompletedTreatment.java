package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.ArchivableBaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompletedTreatment extends ArchivableBaseEntity {

    @NotNull
    @Column(nullable = false)
    LocalDate date;

    @Column(name = "unit_price")
    BigDecimal unitPrice;

    @Type(JsonType.class)
    @Column(name = "tooth_numbers", columnDefinition = "jsonb")
    List<Integer> toothNumbers;

    String notes;

    @Column(name = "procedure_id", nullable = false)
    UUID procedureId;

    @Column(name = "appointment_id")
    UUID appointmentId;

    @Column(name = "treatment_plan_item_id")
    UUID treatmentPlanItemId;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    UUID patientId;

    // TODO: 6/1/2026 decide if we want to also have many to many with payment
}


