package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface ClinicJpaRepository
        extends JpaRepository<Clinic, UUID>, QuerydslPredicateExecutor<Clinic> {
    Optional<Clinic> findByIdAndArchivedFalse(UUID id);
}
