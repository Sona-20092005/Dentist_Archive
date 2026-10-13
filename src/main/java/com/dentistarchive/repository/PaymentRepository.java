package com.dentistarchive.repository;

import com.dentistarchive.entity.patient.Payment;
import com.dentistarchive.repository.jpa.PaymentJpaRepository;
import com.dentistarchive.repository.search.PaymentSearchMapper;
import com.dentistarchive.search.filter.PaymentFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentRepository extends BaseReadOnlyRepository<Payment, PaymentFilter> {

    PaymentJpaRepository jpaRepository;

    public PaymentRepository(
            PaymentSearchMapper searchMapper,
            PaymentJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Payment> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public Payment save(Payment payment) {
        return jpaRepository.save(payment);
    }
    @Transactional(readOnly = true)
    public Optional<UUID> getPatientIdByPaymentId(UUID paymentId) {
        return jpaRepository.findPatientIdByPaymentId(paymentId);
    }
}
