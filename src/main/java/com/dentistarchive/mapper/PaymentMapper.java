package com.dentistarchive.mapper;

import com.dentistarchive.dto.PaymentDto;
import com.dentistarchive.entity.patient.Payment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentMapper implements EntityMapper<Payment, PaymentDto>{

    public PaymentDto toDto(Payment payment) {

        PaymentDto dto = new PaymentDto();

        dto.setId(payment.getId());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setCreatedBy(payment.getCreatedBy());
        dto.setUpdatedAt(payment.getUpdatedAt());
        dto.setUpdatedBy(payment.getUpdatedBy());
        dto.setArchivedAt(payment.getArchivedAt());
        dto.setArchivedBy(payment.getArchivedBy());
        dto.setArchived(payment.isArchived());

        dto.setDate(payment.getDate());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setCommissionAmount(payment.getCommissionAmount());
        dto.setNotes(payment.getNotes());
        dto.setPatientId(payment.getPatientId());

        return dto;
    }
}
