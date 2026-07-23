package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.schedule.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface WorkScheduleJpaRepository
        extends JpaRepository<WorkSchedule, UUID>, QuerydslPredicateExecutor<WorkSchedule> {
    Optional<WorkSchedule> findByIdAndArchivedFalse(UUID id);

    List<WorkSchedule> findByDoctorIdAndArchivedFalse(UUID doctorId);

    List<WorkSchedule> findByClinicIdAndArchivedFalse(UUID clinicId);

    @Query("""
    select ws
    from WorkSchedule ws
    where ws.doctorId = :doctorId
      and ws.effectiveFrom <= :until
      and (
            ws.effectiveUntil is null
            or ws.effectiveUntil >= :from
      )
    """)
    List<WorkSchedule> findByDoctorIdAndEffectivePeriod(UUID doctorId, LocalDate from, LocalDate until);

    @Query(nativeQuery = true,
            value = """
            select doctor_id
            from work_schedule
            where id = :scheduleId
            """
    )
    Optional<UUID> findDoctorIdByScheduleId(@Param("scheduleId") UUID scheduleId);
}
