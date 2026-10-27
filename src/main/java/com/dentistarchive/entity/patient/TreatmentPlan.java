package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.TreatmentPlanStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    @Column(nullable = false)
    LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "plan_status", nullable = false)
    TreatmentPlanStatus planStatus;

    @DecimalMin("0.00")
    @DecimalMax("100.00")
    @Column(name = "discount_percent")
    BigDecimal discountPercent;

    @DecimalMin("0.00")
    @Column(name = "discount_amount")
    BigDecimal discountAmount;

    @NotNull
    @PositiveOrZero
    @Column(name = "final_amount", nullable = false)
    BigDecimal finalAmount;

    // TODO: 6/1/2026 add logic
    @Column(name = "completed_date")
    LocalDate completedDate;

    String notes;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    UUID patientId;

}


