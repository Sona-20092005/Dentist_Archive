package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.patient.CompletedTreatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface CompletedTreatmentJpaRepository
        extends JpaRepository<CompletedTreatment, UUID>, QuerydslPredicateExecutor<CompletedTreatment> {
    Optional<CompletedTreatment> findByIdAndArchivedFalse(UUID id);
}
