package com.dentistarchive.entity.nurse;

import com.dentistarchive.entity.MutableBaseEntity;
import com.dentistarchive.enums.CompensationType;
import com.dentistarchive.enums.NursePayrollStatus;
import com.dentistarchive.utils.MonthConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NursePayroll extends MutableBaseEntity {

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
    @Convert(converter = MonthConverter.class)
    @Column(nullable = false)
    Month month;

    @Builder.Default
    @NotNull
    @Column(name = "calculated_regular_amount", nullable = false)
    BigDecimal calculatedRegularAmount = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @Column(name = "regular_amount", nullable = false)
    BigDecimal regularAmount = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @Column(name = "calculated_overtime_amount", nullable = false)
    BigDecimal calculatedOvertimeAmount = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @Column(name = "overtime_amount", nullable = false)
    BigDecimal overtimeAmount = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @Column(nullable = false)
    BigDecimal bonus = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @Column(nullable = false)
    BigDecimal deduction = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @Column(name = "regular_minutes_worked", nullable = false)
    Integer regularMinutesWorked = 0;

    @Builder.Default
    @NotNull
    @Column(name = "overtime_minutes_worked", nullable = false)
    Integer overtimeMinutesWorked = 0;

    LocalDate paymentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "compensation_type", nullable = false)
    CompensationType compensationType;

    @NotNull
    @Column(nullable = false)
    Boolean locked;

    String notes;

    public boolean isManuallyAdjusted() {
        return regularAmount.compareTo(calculatedRegularAmount) != 0
                || overtimeAmount.compareTo(calculatedOvertimeAmount) != 0
                || bonus.compareTo(BigDecimal.ZERO) != 0
                || deduction.compareTo(BigDecimal.ZERO) != 0;
    }


    public boolean isRegularAmountManuallyAdjusted() {
        return regularAmount.compareTo(calculatedRegularAmount) != 0;
    }

    public boolean isOvertimeAmountManuallyAdjusted() {
        return overtimeAmount.compareTo(calculatedOvertimeAmount) != 0;
    }



}



