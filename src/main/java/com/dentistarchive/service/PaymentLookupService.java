package com.dentistarchive.service;

import com.dentistarchive.entity.patient.Payment;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.PaymentRepository;
import com.dentistarchive.service.access.PaymentAccessValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentLookupService {
    PaymentRepository paymentRepository;
    PaymentAccessValidator paymentAccessValidator;

    @Transactional(readOnly = true)
    public Payment getAccessiblePayment(UUID id) {
        if (id == null) {
            return null;
        }

        Payment payment = paymentRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Payment.class, id));

        paymentAccessValidator.validateAccess(payment);

        return payment;
    }
}