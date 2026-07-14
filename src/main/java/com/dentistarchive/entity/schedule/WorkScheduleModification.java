package com.dentistarchive.entity.schedule;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.ModificationType;
import com.dentistarchive.enums.WorkSessionType;
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
public class WorkScheduleModification extends ArchivableBaseEntity {

    @Column()
    LocalDate date;

    @Column(name = "start_time")
    LocalTime startTime;

    @Column(name = "end_time")
    LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "modification_type", nullable = false)
    ModificationType modificationType;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "work_session_type", nullable = false)
    WorkSessionType workSessionType;

    @Column(name = "source_date")
    LocalDate sourceDate;

    @Column(name = "source_start_time")
    LocalTime sourceStartTime;

    @Column(name = "source_end_time")
    LocalTime sourceEndTime;

    @NotNull
    @Column(name = "schedule_id", nullable = false)
    UUID scheduleId;
}


