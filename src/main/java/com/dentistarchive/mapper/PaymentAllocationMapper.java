package com.dentistarchive.mapper;

import com.dentistarchive.dto.PaymentAllocationDto;
import com.dentistarchive.entity.patient.PaymentAllocation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentAllocationMapper implements EntityMapper<PaymentAllocation, PaymentAllocationDto>{

    public PaymentAllocationDto toDto(PaymentAllocation allocation) {

        PaymentAllocationDto dto = new PaymentAllocationDto();

        dto.setId(allocation.getId());
        dto.setCreatedAt(allocation.getCreatedAt());
        dto.setCreatedBy(allocation.getCreatedBy());
        dto.setUpdatedAt(allocation.getUpdatedAt());
        dto.setUpdatedBy(allocation.getUpdatedBy());
        dto.setArchivedAt(allocation.getArchivedAt());
        dto.setArchivedBy(allocation.getArchivedBy());
        dto.setArchived(allocation.isArchived());

        dto.setPaymentId(allocation.getPaymentId());
        dto.setAmount(allocation.getAmount());
        dto.setCompletedTreatmentId(allocation.getCompletedTreatmentId());
        dto.setTreatmentPlanId(allocation.getTreatmentPlanId());
        dto.setArchiveReason(allocation.getArchiveReason());

        return dto;
    }
}
