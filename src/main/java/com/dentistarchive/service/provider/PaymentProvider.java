package com.dentistarchive.service.provider;

import com.dentistarchive.dto.create.PaymentCreateDto;
import com.dentistarchive.dto.update.PaymentUpdateDto;
import com.dentistarchive.entity.patient.Payment;
import org.springframework.stereotype.Component;


@Component
public class PaymentProvider {

    public Payment create(PaymentCreateDto createDto) {

        Payment payment = new Payment();
        payment.setDate(createDto.getDate());
        payment.setAmount(createDto.getAmount());
        payment.setPaymentMethod(createDto.getPaymentMethod());
        payment.setCommissionAmount(createDto.getCommissionAmount());
        payment.setNotes(createDto.getNotes());
        payment.setPatientId(createDto.getPatientId());

        return payment;
    }

    public Payment update(Payment payment, PaymentUpdateDto updateDto) {

        payment.setDate(updateDto.getDate());
        payment.setAmount(updateDto.getAmount());
        payment.setPaymentMethod(updateDto.getPaymentMethod());
        payment.setCommissionAmount(updateDto.getCommissionAmount());
        payment.setNotes(updateDto.getNotes());
        payment.setPatientId(updateDto.getPatientId());

        return payment;
    }
}
