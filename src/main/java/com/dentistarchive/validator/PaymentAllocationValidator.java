package com.dentistarchive.validator;

import com.dentistarchive.entity.patient.Payment;
import com.dentistarchive.entity.patient.PaymentAllocation;
import com.dentistarchive.repository.PaymentAllocationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class PaymentAllocationValidator {
    // TODO: 10/15/2026 exceptions
    PaymentAllocationRepository paymentAllocationRepository;

    public void validate(PaymentAllocation allocation, Payment payment, BigDecimal targetCharge) {
        validateTarget(allocation);
        validateAmount(allocation);
        validatePaymentAmount(allocation, payment);
        validateTargetCharge(allocation, targetCharge);
    }

    public void validateUnarchive(PaymentAllocation allocation, Payment payment, BigDecimal targetCharge) {
        validatePaymentAmount(allocation, payment);
        validateTargetCharge(allocation, targetCharge);
    }

    private void validateTarget(PaymentAllocation allocation) {
        if((allocation.getTreatmentPlanId() == null && allocation.getCompletedTreatmentId() == null) ||
                (allocation.getTreatmentPlanId() != null && allocation.getCompletedTreatmentId() != null)) {
            throw new IllegalStateException("Allocation refers to either a treatment plan or completed treatment");
        }
    }

    private void validateAmount(PaymentAllocation allocation) {
        if (allocation.getAmount() == null
                || allocation.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Allocation amount must be greater than zero");
        }
    }

    private void validatePaymentAmount(PaymentAllocation allocation, Payment payment) {
        BigDecimal alreadyAllocated = paymentAllocationRepository.sumActiveByPaymentIdExcludingId(payment.getId(), allocation.getId());
        BigDecimal totalAllocated = alreadyAllocated.add(allocation.getAmount());

        if (totalAllocated.compareTo(payment.getAmount()) > 0) {
            throw new IllegalStateException("Total allocations cannot exceed the payment amount");
        }
    }

    private void validateTargetCharge(PaymentAllocation allocation, BigDecimal targetCharge) {
        BigDecimal alreadyAllocated;

        if (allocation.getTreatmentPlanId() != null) {
            alreadyAllocated = paymentAllocationRepository.sumActiveByTreatmentPlanIdExcludingId(
                                    allocation.getTreatmentPlanId(),
                                    allocation.getId()
                            );
        }
        else {
            alreadyAllocated = paymentAllocationRepository.sumActiveByCompletedTreatmentIdExcludingId(
                                    allocation.getCompletedTreatmentId(),
                                    allocation.getId()
                            );
        }

        BigDecimal totalAllocated = alreadyAllocated.add(allocation.getAmount());

        if (totalAllocated.compareTo(targetCharge) > 0) {
            throw new IllegalStateException("Total allocations cannot exceed the target charge");
        }
    }

}

