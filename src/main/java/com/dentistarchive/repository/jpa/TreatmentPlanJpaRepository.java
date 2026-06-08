package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.patient.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface TreatmentPlanJpaRepository
        extends JpaRepository<TreatmentPlan, UUID>, QuerydslPredicateExecutor<TreatmentPlan> {
    Optional<TreatmentPlan> findByIdAndArchivedFalse(UUID id);

    @Query(nativeQuery = true,
            value = """
            select patient_id
            from treatment_plan
            where id = :planId
            """
    )
    Optional<UUID> findPatientIdByPlanId(@Param("planId") UUID planId);
}
