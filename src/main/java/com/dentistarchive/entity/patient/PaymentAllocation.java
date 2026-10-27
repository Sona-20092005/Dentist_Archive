package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.PaymentAllocationArchiveReason;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentAllocation extends ArchivableBaseEntity {

    @NotNull
    @Column(name = "payment_id", nullable = false)
    UUID paymentId;

    @NotNull
    @Positive
    @Column(nullable = false)
    BigDecimal amount;

    @Column(name = "treatment_plan_id")
    UUID treatmentPlanId;

    @Column(name = "completed_treatment_id")
    UUID completedTreatmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "archive_reason")
    PaymentAllocationArchiveReason archiveReason;
}


