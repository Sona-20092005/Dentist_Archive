package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.TreatmentPlanStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentPlan extends ArchivableBaseEntity {

    @NotNull
    LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_status")
    TreatmentPlanStatus planStatus;

    @Column(name = "discount_percent")
    BigDecimal discountPercent;

    @Column(name = "discount_amount")
    BigDecimal discountAmount;

    String notes;

    @Column(name = "patient_id", nullable = false)
    UUID patientId;
}


