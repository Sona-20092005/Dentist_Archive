package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.schedule.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface WorkScheduleJpaRepository
        extends JpaRepository<WorkSchedule, UUID>, QuerydslPredicateExecutor<WorkSchedule> {
    Optional<WorkSchedule> findByIdAndArchivedFalse(UUID id);

    List<WorkSchedule> findByDoctorIdAndArchivedFalse(UUID doctorId);

    List<WorkSchedule> findByClinicIdAndArchivedFalse(UUID clinicId);
}
