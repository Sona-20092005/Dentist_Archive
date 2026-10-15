package com.dentistarchive.repository;

import com.dentistarchive.entity.patient.PaymentAllocation;
import com.dentistarchive.repository.jpa.PaymentAllocationJpaRepository;
import com.dentistarchive.repository.search.PaymentAllocationSearchMapper;
import com.dentistarchive.search.filter.PaymentAllocationFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentAllocationRepository extends BaseReadOnlyRepository<PaymentAllocation, PaymentAllocationFilter> {

    PaymentAllocationJpaRepository jpaRepository;

    public PaymentAllocationRepository(
            PaymentAllocationSearchMapper searchMapper,
            PaymentAllocationJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<PaymentAllocation> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse(id);
    }

    @Transactional
    public PaymentAllocation save(PaymentAllocation paymentAllocation) {
        return jpaRepository.save(paymentAllocation);
    }

    @Transactional(readOnly = true)
    public List<PaymentAllocation> getPaymentAllocationsByPaymentId(UUID paymentId) {
        return jpaRepository.findAllByPaymentIdAndArchivedFalse(paymentId);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumActiveByPaymentIdExcludingId(UUID paymentId, UUID excludeId) {
        return jpaRepository.sumActiveByPaymentIdExcludingId(paymentId, excludeId);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumActiveByTreatmentPlanIdExcludingId(UUID planId, UUID excludeId) {
        return jpaRepository.sumActiveByTreatmentPlanIdExcludingId(planId, excludeId);
    }

    @Transactional(readOnly = true)
    public BigDecimal sumActiveByCompletedTreatmentIdExcludingId(UUID treatmentId, UUID excludeId) {
        return jpaRepository.sumActiveByCompletedTreatmentIdExcludingId(treatmentId, excludeId);
    }

}
