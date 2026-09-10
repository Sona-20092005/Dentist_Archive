package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.technician.TechnicianProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface TechnicianProcedureJpaRepository
        extends JpaRepository<TechnicianProcedure, UUID>, QuerydslPredicateExecutor<TechnicianProcedure> {
    Optional<TechnicianProcedure> findByIdAndArchivedFalse(UUID id);
}
