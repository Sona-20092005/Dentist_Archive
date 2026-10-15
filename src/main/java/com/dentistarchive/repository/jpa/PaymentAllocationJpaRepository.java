package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.patient.PaymentAllocation;
import com.dentistarchive.enums.PaymentAllocationArchiveReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface PaymentAllocationJpaRepository
        extends JpaRepository<PaymentAllocation, UUID>, QuerydslPredicateExecutor<PaymentAllocation> {
    Optional<PaymentAllocation> findByIdAndArchivedFalse(UUID id);

    List<PaymentAllocation> findAllByPaymentIdAndArchivedFalse(UUID paymentId);

    List<PaymentAllocation> findAllByPaymentIdAndArchivedTrueAndArchiveReason(
            UUID paymentId,
            PaymentAllocationArchiveReason archiveReason
    );

    @Query("""
        select coalesce(sum(pa.amount), 0)
        from PaymentAllocation pa
        where pa.paymentId = :paymentId
          and pa.archived = false
          and (:excludeId is null or pa.id <> :excludeId)
        """)
    BigDecimal sumActiveByPaymentIdExcludingId(
            @Param("paymentId") UUID paymentId,
            @Param("excludeId") UUID excludeId
    );

    @Query("""
        select coalesce(sum(pa.amount), 0)
        from PaymentAllocation pa
        where pa.treatmentPlanId = :planId
          and pa.archived = false
          and (:excludeId is null or pa.id <> :excludeId)
        """)
    BigDecimal sumActiveByTreatmentPlanIdExcludingId(
            @Param("planId") UUID planId,
            @Param("excludeId") UUID excludeId
    );

    @Query("""
        select coalesce(sum(pa.amount), 0)
        from PaymentAllocation pa
        where pa.completedTreatmentId = :treatmentId
          and pa.archived = false
          and (:excludeId is null or pa.id <> :excludeId)
        """)
    BigDecimal sumActiveByCompletedTreatmentIdExcludingId(
            @Param("treatmentId") UUID treatmentId,
            @Param("excludeId") UUID excludeId
    );


}
