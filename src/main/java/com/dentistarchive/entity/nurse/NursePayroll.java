package com.dentistarchive.entity.nurse;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.CompensationType;
import com.dentistarchive.enums.NursePayrollStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

@Data
//@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NursePayroll extends ArchivableBaseEntity {

    @NonNull
    @Column(name = "nurse_id", nullable = false)
    UUID nurseId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    NursePayrollStatus status;

    @NotNull
    @Column(nullable = false)
    Integer year;

    @NotNull
    @Column(nullable = false)
    Month month;

    @NotNull
    @Column(name = "calculated_regular_amount", nullable = false)
    BigDecimal calculatedRegularAmount;

    @NotNull
    @Column(name = "regular_amount", nullable = false)
    BigDecimal regularAmount;

    @NotNull
    @Column(name = "calculated_overtime_amount", nullable = false)
    BigDecimal calculatedOvertimeAmount;

    @NotNull
    @Column(name = "overtime_amount", nullable = false)
    BigDecimal overtimeAmount;

    BigDecimal bonus;

    BigDecimal deduction;

    @NotNull
    @Column(name = "regular_minutes_worked", nullable = false)
    Integer regularMinutesWorked;

    @NotNull
    @Column(name = "overtime_minutes_worked", nullable = false)
    Integer overtimeMinutesWorked;

    LocalDate paymentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "compensation_type", nullable = false)
    CompensationType compensationType;

    @NotNull
    @Column(name = "has_manually_adjustments", nullable = false)
    Boolean hasManuallyAdjustments;

    @NotNull
    @Column(nullable = false)
    Boolean locked;

    String notes;

}



