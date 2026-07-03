package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.schedule.WorkScheduleRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface WorkScheduleRuleJpaRepository
        extends JpaRepository<WorkScheduleRule, UUID>, QuerydslPredicateExecutor<WorkScheduleRule> {
    Optional<WorkScheduleRule> findByIdAndArchivedFalse(UUID id);

    List<WorkScheduleRule> findByScheduleIdAndArchivedFalse(UUID scheduleId);

    List<WorkScheduleRule> findByScheduleIdInAndArchivedFalse(Collection<UUID> scheduleIds);

    List<WorkScheduleRule> findByScheduleIdAndDayOfWeekAndArchivedFalse(UUID scheduleId, DayOfWeek dayOfWeek);
}
