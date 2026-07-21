package com.dentistarchive.entity.schedule;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.AppointmentSchedulingStatus;
import com.dentistarchive.enums.AppointmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class Appointment extends ArchivableBaseEntity {

    @NotNull
    @Column(nullable = false)
    LocalDate date;

    @NotNull
    @Column(name = "start_time", nullable = false)
    LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    LocalTime endTime;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status", nullable = false)
    AppointmentStatus appointmentStatus;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_scheduling_status", nullable = false)
    AppointmentSchedulingStatus appointmentSchedulingStatus;

    String notes;

    @Column(name = "nurse_id")
    UUID nurseId;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    UUID patientId;

    @NotNull
    @Column(name = "schedule_id", nullable = false)
    UUID scheduleId;
}
