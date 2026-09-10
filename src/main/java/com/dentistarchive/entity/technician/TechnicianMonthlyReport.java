package com.dentistarchive.entity.technician;

import com.dentistarchive.entity.MutableBaseEntity;
import com.dentistarchive.enums.TechnicianMonthlyReportStatus;
import com.dentistarchive.utils.YearMonthDateConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TechnicianMonthlyReport extends MutableBaseEntity {

    @NonNull
    @Column(name = "technician_id", nullable = false)
    UUID technicianId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    TechnicianMonthlyReportStatus status;

    @NotNull
    @Convert(converter = YearMonthDateConverter.class)
    @Column(nullable = false)
    YearMonth yearMonth;

    @Builder.Default
    @NotNull
    @Column(name = "total_amount", nullable = false)
    BigDecimal totalAmount = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @Column(name = "paid_amount", nullable = false)
    BigDecimal paidAmount = BigDecimal.ZERO;

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
    @Column(name = "number_of_work_logs", nullable = false)
    Integer numberOfWorkLogs = 0;

    @NotNull
    @Column(nullable = false)
    Boolean locked;

    String notes;
}



