package com.dentistarchive.entity.nurse;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.NurseWorkType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NurseWorkLog extends ArchivableBaseEntity {

    @NonNull
    @Column(name = "nurse_id", nullable = false)
    UUID nurseId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "work_type", nullable = false)
    NurseWorkType workType;

    @NotNull
    @Column(nullable = false)
    LocalDate date;

    @NotNull
    @Column(name = "start_time", nullable = false)
    LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    LocalTime endTime;

    @NotNull
    @Column(name = "hourly_rate")
    BigDecimal hourlyRate;

    String notes;

}



