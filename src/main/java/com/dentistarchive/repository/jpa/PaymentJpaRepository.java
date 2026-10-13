package com.dentistarchive.repository.jpa;

import com.dentistarchive.entity.patient.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;


public interface PaymentJpaRepository
        extends JpaRepository<Payment, UUID>, QuerydslPredicateExecutor<Payment> {
    Optional<Payment> findByIdAndArchivedFalse(UUID id);

    @Query(nativeQuery = true,
            value = """
            select patient_id
            from payment
            where id = :paymentId
            """
    )
    Optional<UUID> findPatientIdByPaymentId(@Param("paymentId") UUID paymentId);
}
