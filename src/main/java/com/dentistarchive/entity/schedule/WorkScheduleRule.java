package com.dentistarchive.entity.schedule;

import com.dentistarchive.entity.ArchivableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkScheduleRule extends ArchivableBaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    DayOfWeek dayOfWeek;

    @NotNull
    @Column(name = "day_of_week_sort_order", nullable = false)
    Integer dayOfWeekSortOrder;

    @NotNull
    @Column(name = "start_time", nullable = false)
    LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    LocalTime endTime;

    String notes;

    @NotNull
    @Column(name = "schedule_id", nullable = false)
    UUID scheduleId;
}


