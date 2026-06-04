package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.patient.TreatmentPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.UUID;


public interface TreatmentPlanItemJpaRepository
        extends JpaRepository<TreatmentPlanItem, UUID>, QuerydslPredicateExecutor<TreatmentPlanItem> {
    Optional<TreatmentPlanItem> findByIdAndArchivedFalse(UUID id);
}
