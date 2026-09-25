package com.dentistarchive.entity.rent;

import com.dentistarchive.entity.MutableBaseEntity;
import com.dentistarchive.enums.RentMonthlyReportStatus;
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
public class RentMonthlyReport extends MutableBaseEntity {

    @NotNull
    @Column(name = "clinic_rent_agreement_id", nullable = false)
    UUID clinicRentAgreementId;

    @NotNull
    @Convert(converter = YearMonthDateConverter.class)
    @Column(name = "year_month", nullable = false)
    YearMonth yearMonth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RentMonthlyReportStatus status;

    @NotNull
    @Builder.Default
    @Column(name = "regular_amount", nullable = false)
    BigDecimal regularAmount = BigDecimal.ZERO;

    @NotNull
    @Builder.Default
    @Column(name = "overtime_amount", nullable = false)
    BigDecimal overtimeAmount = BigDecimal.ZERO;

    @Builder.Default
    @NotNull
    @Column(name = "regular_minutes_used", nullable = false)
    Integer regularMinutesUsed = 0;

    @Builder.Default
    @NotNull
    @Column(name = "overtime_minutes_used", nullable = false)
    Integer overtimeMinutesUsed = 0;

    @Builder.Default
    @NotNull
    @Column(name = "manual_adjustment", nullable = false)
    BigDecimal manualAdjustment = BigDecimal.ZERO;

    @NotNull
    @Column(nullable = false)
    Boolean locked;

    String notes;


    // TODO: 9/17/2026 add later 
//    @NotNull
//    @Builder.Default
//    @Column(name = "collection_amount", nullable = false)
//    BigDecimal collectionAmount = BigDecimal.ZERO;
//
//    @NotNull
//    @Builder.Default
//    @Column(name = "collection_rent_amount", nullable = false)
//    BigDecimal collectionRentAmount = BigDecimal.ZERO;

    // TODO: 9/17/2026 add this later too 
//    @Builder.Default
//    @NotNull
//    @Column(name = "paid_amount", nullable = false)
//    BigDecimal paidAmount = BigDecimal.ZERO;
//
//    LocalDate paymentDate;



}



