package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.PaymentAllocationCreateDto;
import com.dentistarchive.dto.update.PaymentAllocationUpdateDto;
import com.dentistarchive.entity.patient.PaymentAllocation;
import org.springframework.stereotype.Component;


@Component
public class PaymentAllocationProvider {

    public PaymentAllocation create(PaymentAllocationCreateDto createDto) {

        PaymentAllocation allocation = new PaymentAllocation();
        allocation.setPaymentId(createDto.getPaymentId());
        allocation.setAmount(createDto.getAmount());
        allocation.setCompletedTreatmentId(createDto.getCompletedTreatmentId());
        allocation.setTreatmentPlanId(createDto.getTreatmentPlanId());

        return allocation;
    }

    public PaymentAllocation update(PaymentAllocation allocation, PaymentAllocationUpdateDto updateDto) {

        allocation.setAmount(updateDto.getAmount());
        allocation.setCompletedTreatmentId(updateDto.getCompletedTreatmentId());
        allocation.setTreatmentPlanId(updateDto.getTreatmentPlanId());

        return allocation;
    }
}
