package com.dentistarchive.entity.schedule;

import com.dentistarchive.entity.ArchivableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkSchedule extends ArchivableBaseEntity {

    String name;

    String notes;

    @NotNull
    @Column(name = "effective_from", nullable = false)
    LocalDate effectiveFrom;

    @Column(name = "effective_until")
    LocalDate effectiveUntil;

    @NotNull
    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;

    @NotNull
    @Column(name = "clinic_id", nullable = false)
    UUID clinicId;
}


