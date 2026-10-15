package com.dentistarchive.service.access;

import com.dentistarchive.entity.patient.PaymentAllocation;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.repository.PaymentRepository;
import com.dentistarchive.search.filter.PaymentAllocationFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentAllocationAccessValidator extends BaseReadOnlyAccessValidator<PaymentAllocation, PaymentAllocationFilter> {
    private final PaymentRepository paymentRepository;
    private final PatientRepository patientRepository;

    @Override
    protected PaymentAllocationFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return PaymentAllocationFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(PaymentAllocation entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return paymentRepository
                .getPatientIdByPaymentId(entity.getPaymentId())
                .flatMap(patientRepository::getDoctorIdByPatientId)
                .map(details.getUserId()::equals)
                .orElse(false);
    }

    @Override
    protected Class<PaymentAllocationFilter> getFilterClass() {
        return PaymentAllocationFilter.class;
    }
}
