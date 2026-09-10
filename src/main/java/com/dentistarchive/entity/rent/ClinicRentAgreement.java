package com.dentistarchive.entity.rent;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.RentCalculationType;
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
public class ClinicRentAgreement extends ArchivableBaseEntity {

    @NotNull
    @Column(name = "clinic_id", nullable = false)
    UUID clinicId;

    @NotNull
    @Convert(converter = YearMonthDateConverter.class)
    @Column(name = "effective_from", nullable = false)
    YearMonth effectiveFrom;

    @Convert(converter = YearMonthDateConverter.class)
    @Column(name = "effective_until")
    YearMonth effectiveUntil;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "calculation_type", nullable = false)
    RentCalculationType rentCalculationType;

    @Column(name = "fixed_monthly_rent")
    BigDecimal fixedMonthlyRent;

    @Column(name = "hourly_rate")
    BigDecimal hourlyRate;

    @Column(name = "overtime_hourly_rate")
    BigDecimal overtimeHourlyRate;

    @Column(name = "collection_percentage")
    Integer collectionPercentage;

    String notes;
}

