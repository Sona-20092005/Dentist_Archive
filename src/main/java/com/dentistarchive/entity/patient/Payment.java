package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class Payment extends ArchivableBaseEntity {

    @NotNull
    @Column(nullable = false)
    LocalDate date;

    @NotNull
    @Positive
    @Column(nullable = false)
    BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    PaymentMethod paymentMethod;

    @NotNull
    @PositiveOrZero
    @Builder.Default
    @Column(name = "commission_amount", nullable = false)
    BigDecimal commissionAmount = BigDecimal.ZERO;

    String notes;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    UUID patientId;
}


