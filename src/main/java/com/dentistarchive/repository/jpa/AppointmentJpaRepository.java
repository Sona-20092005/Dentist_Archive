package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.schedule.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface AppointmentJpaRepository
        extends JpaRepository<Appointment, UUID>, QuerydslPredicateExecutor<Appointment> {
    Optional<Appointment> findByIdAndArchivedFalse(UUID id);
}
