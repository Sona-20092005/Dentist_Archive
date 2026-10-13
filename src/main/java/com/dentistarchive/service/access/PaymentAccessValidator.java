package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.Payment;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.search.filter.PaymentFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentAccessValidator extends BaseReadOnlyAccessValidator<Payment, PaymentFilter> {
    final PatientRepository patientRepository;

    @Override
    protected PaymentFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return PaymentFilter.builder()
                .doctorId(details.getUserId())
                .build();
    }

    @Override
    protected boolean hasAccess(Payment entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return patientRepository
                .getDoctorIdByPatientId(entity.getPatientId())
                .map(details.getUserId()::equals)
                .orElse(false);
   }

    @Override
    protected Class<PaymentFilter> getFilterClass() {
        return PaymentFilter.class;
    }
}
