package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.schedule.WorkScheduleModification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface WorkScheduleModificationJpaRepository
        extends JpaRepository<WorkScheduleModification, UUID>, QuerydslPredicateExecutor<WorkScheduleModification> {
    Optional<WorkScheduleModification> findByIdAndArchivedFalse(UUID id);

    List<WorkScheduleModification> findByScheduleIdAndDateAndArchivedFalse(UUID scheduleId, LocalDate date);
}
