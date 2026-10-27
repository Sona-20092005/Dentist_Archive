package com.dentistarchive.validator;

import com.dentistarchive.entity.patient.Payment;
import com.dentistarchive.entity.patient.PaymentAllocation;
import com.dentistarchive.repository.PaymentAllocationRepository;
import com.dentistarchive.service.CompletedTreatmentService;
import com.dentistarchive.service.PaymentService;
import com.dentistarchive.service.TreatmentPlanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.ToDoubleBiFunction;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class PaymentAllocationValidator {
    // TODO: 10/15/2026 exceptions
    PaymentAllocationRepository paymentAllocationRepository;
    TreatmentPlanService treatmentPlanService;
    CompletedTreatmentService completedTreatmentService;

    public void validate(PaymentAllocation allocation, Payment payment) {
        validateTarget(allocation);
        validateAmount(allocation);
        validatePatient(allocation, payment);
        validatePaymentAmount(allocation, payment);
        validateTargetCharge(allocation);
    }

    public void validateUnarchive(PaymentAllocation allocation, Payment payment) {
        validatePatient(allocation, payment);
        validatePaymentAmount(allocation, payment);
        validateTargetCharge(allocation);
    }


    private void validateTarget(PaymentAllocation allocation) {
        if((allocation.getTreatmentPlanId() == null && allocation.getCompletedTreatmentId() != null) ||
                (allocation.getTreatmentPlanId() != null && allocation.getCompletedTreatmentId() == null)) {
            throw new IllegalStateException("Allocation refers to either a treatment plan or completed treatment");
        }
    }

    private void validateAmount(PaymentAllocation allocation) {
        if (allocation.getAmount() == null
                || allocation.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Allocation amount must be greater than zero");
        }
    }

    private void validatePatient(PaymentAllocation allocation, Payment payment) {
        if (allocation.getTreatmentPlanId() != null) {
            var treatmentPlan = treatmentPlanService.getAccessibleTreatmentPlan(allocation.getTreatmentPlanId());

            if (!payment.getPatientId().equals(treatmentPlan.getPatientId())) {
                throw new IllegalArgumentException("Payment and treatment plan must belong to the same patient");
            }
        }

        if (allocation.getCompletedTreatmentId() != null) {
            var completedTreatment = completedTreatmentService
                    .getAccessibleCompletedTreatment(allocation.getCompletedTreatmentId());

            if (!payment.getPatientId().equals(completedTreatment.getPatientId())) {
                throw new IllegalArgumentException("Payment and completed treatment must belong to the same patient");
            }
        }
    }

    private void validatePaymentAmount(PaymentAllocation allocation, Payment payment) {
        BigDecimal alreadyAllocated = paymentAllocationRepository.sumActiveByPaymentIdExcludingId(payment.getId(), allocation.getId());
        BigDecimal totalAllocated = alreadyAllocated.add(allocation.getAmount());

        if (totalAllocated.compareTo(payment.getAmount()) > 0) {
            throw new IllegalStateException("Total allocations cannot exceed the payment amount");
        }
    }

    private void validateTargetCharge(PaymentAllocation allocation) {
        BigDecimal alreadyAllocated;
        BigDecimal targetCharge;

        if (allocation.getTreatmentPlanId() != null) {
            alreadyAllocated = paymentAllocationRepository.sumActiveByTreatmentPlanIdExcludingId(
                                    allocation.getTreatmentPlanId(),
                                    allocation.getId()
                            );
            var treatmentPlan = treatmentPlanService.getAccessibleTreatmentPlan(allocation.getTreatmentPlanId());
            targetCharge = treatmentPlan.getDiscountAmount(); //wrong logic placeholder, remove after fixing
//
            // TODO: 10/20/2026 come here after taking care of treatment plan to add the validation
        }

        else {
            alreadyAllocated = paymentAllocationRepository.sumActiveByCompletedTreatmentIdExcludingId(
                                    allocation.getCompletedTreatmentId(),
                                    allocation.getId()
                            );
            var completedTreatment = completedTreatmentService.getAccessibleCompletedTreatment(allocation.getCompletedTreatmentId());
            if(completedTreatment.getUnitPrice() == null) {
                throw new IllegalArgumentException("Completed treatment is not charged");
            }
            targetCharge = completedTreatment.getUnitPrice().multiply(completedTreatment.getUnitPrice());
        }

        BigDecimal totalAllocated = alreadyAllocated.add(allocation.getAmount());

        if (totalAllocated.compareTo(targetCharge) > 0) {
            throw new IllegalStateException("Total allocations cannot exceed the target charge");
        }
    }

}

