package com.dentistarchive.service;

import com.dentistarchive.dto.create.PaymentCreateDto;
import com.dentistarchive.dto.update.PaymentUpdateDto;
import com.dentistarchive.entity.patient.Payment;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.PaymentRepository;
import com.dentistarchive.search.filter.PaymentFilter;
import com.dentistarchive.service.access.PaymentAccessValidator;
import com.dentistarchive.service.provider.PaymentProvider;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService extends BaseReadOnlyService<Payment, PaymentFilter>
        implements ArchivableService<Payment, PaymentFilter> {
    PaymentRepository paymentRepository;
    PaymentAccessValidator accessValidator;
    PaymentProvider paymentProvider;
    PatientService patientService;

    public PaymentService(
            PaymentRepository paymentRepository,
            PaymentAccessValidator accessValidator,
            PaymentProvider paymentProvider,
            PatientService patientService
    ) {
        super(
                Payment.class,
                PaymentFilter.class,
                paymentRepository,
                accessValidator
        );
        this.paymentRepository = paymentRepository;
        this.accessValidator = accessValidator;
        this.paymentProvider = paymentProvider;
        this.patientService = patientService;
    }

    @Transactional
    public Payment create(PaymentCreateDto createDto) {
        patientService.getAccessiblePatient(createDto.getPatientId());

        var payment = paymentProvider.create(createDto);
        return save(payment);
    }

    @Transactional(propagation = Propagation.NEVER)
    public Payment update(UUID id, @Valid PaymentUpdateDto updateDto) {
        Payment payment = paymentRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Payment.class, id));

        patientService.getAccessiblePatient(updateDto.getPatientId());

        accessValidator.validateAccess(payment);
        paymentProvider.update(payment, updateDto);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment save(Payment entity) {
        return paymentRepository.save(entity);
    }

    @Override
    public void afterArchive(Payment entity) {}

    @Override
    public void afterUnarchive(Payment entity) {}

    public Payment getAccessiblePayment(UUID id) {
        if (id == null) {
            return null;
        }

        Payment payment = paymentRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Payment.class, id));

        accessValidator.validateAccess(payment);

        return payment;
    }

}
