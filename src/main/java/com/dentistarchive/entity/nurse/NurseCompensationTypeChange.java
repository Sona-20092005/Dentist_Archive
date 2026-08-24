package com.dentistarchive.entity.nurse;

import com.dentistarchive.entity.MutableBaseEntity;
import com.dentistarchive.enums.CompensationType;
import com.dentistarchive.utils.YearMonthDateConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.YearMonth;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NurseCompensationTypeChange extends MutableBaseEntity {

    @NotNull
    @Column(name = "nurse_id", nullable = false)
    UUID nurseId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "compensation_type", nullable = false)
    CompensationType compensationType;

    @NotNull
    @Convert(converter = YearMonthDateConverter.class)
    @Column(name = "effective_from", nullable = false)
    YearMonth effectiveFrom;

}
