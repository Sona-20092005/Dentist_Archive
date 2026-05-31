package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.patient.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface TreatmentPlanJpaRepository
        extends JpaRepository<TreatmentPlan, UUID>, QuerydslPredicateExecutor<TreatmentPlan> {
    Optional<TreatmentPlan> findByIdAndArchivedFalse(UUID id);
}
