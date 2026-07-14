package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.schedule.WorkScheduleModification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface WorkScheduleModificationJpaRepository
        extends JpaRepository<WorkScheduleModification, UUID>, QuerydslPredicateExecutor<WorkScheduleModification> {
    Optional<WorkScheduleModification> findByIdAndArchivedFalse(UUID id);

    @Query("""
    select m
    from WorkScheduleModification m
    where m.scheduleId = :scheduleId
      and m.archived = false
      and (
            m.date = :date
         or m.sourceDate = :date
      )
    order by m.createdAt asc
    """)
    List<WorkScheduleModification> findEffectiveModifications(@Param("scheduleId") UUID scheduleId, @Param("date") LocalDate date);
}
