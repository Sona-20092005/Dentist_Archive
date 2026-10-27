package com.dentistarchive.service;

import com.dentistarchive.dto.create.PaymentCreateDto;
import com.dentistarchive.dto.update.PaymentUpdateDto;
import com.dentistarchive.entity.patient.Payment;
import com.dentistarchive.entity.patient.PaymentAllocation;
import com.dentistarchive.enums.PaymentAllocationArchiveReason;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.PaymentAllocationRepository;
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

import java.util.List;
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
    PaymentAllocationService paymentAllocationService;
    PaymentAllocationRepository paymentAllocationRepository;


    public PaymentService(
            PaymentRepository paymentRepository,
            PaymentAccessValidator accessValidator,
            PaymentProvider paymentProvider,
            PatientService patientService,
            PaymentAllocationService paymentAllocationService,
            PaymentAllocationRepository paymentAllocationRepository
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
        this.paymentAllocationService = paymentAllocationService;
        this.paymentAllocationRepository = paymentAllocationRepository;
    }

    @Transactional
    public Payment create(@Valid PaymentCreateDto createDto) {
        patientService.getAccessiblePatient(createDto.getPatientId());

        // TODO: 10/22/2026 exceptions

        if(createDto.getCommissionAmount().compareTo(createDto.getAmount()) > 0) {
            throw new IllegalArgumentException("commission amount cannot be more than paid amount");
        }

        var payment = paymentProvider.create(createDto);
        return save(payment);
    }

    @Transactional(propagation = Propagation.NEVER)
    public Payment update(UUID id, @Valid PaymentUpdateDto updateDto) {
        Payment payment = paymentRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Payment.class, id));

        patientService.getAccessiblePatient(updateDto.getPatientId());

        accessValidator.validateAccess(payment);

        if(updateDto.getCommissionAmount().compareTo(updateDto.getAmount()) > 0) {
            throw new IllegalArgumentException("commission amount cannot be more than paid amount");
        }

        paymentProvider.update(payment, updateDto);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment save(Payment entity) {
        return paymentRepository.save(entity);
    }

    @Transactional
    @Override
    public void afterArchive(Payment entity) {
        List<PaymentAllocation> allocations = paymentAllocationRepository.getPaymentAllocationsByPaymentId(entity.getId());

        for (PaymentAllocation allocation : allocations) {
            paymentAllocationService.archiveById(
                    allocation.getId(),
                    PaymentAllocationArchiveReason.PAYMENT_ARCHIVED
            );
        }
    }

    @Override
    public void afterUnarchive(Payment entity) {
        List<PaymentAllocation> allocations = paymentAllocationRepository.getAllArchivedByPaymentIdAndReason(
                entity.getId(),
                PaymentAllocationArchiveReason.PAYMENT_ARCHIVED);

        for (PaymentAllocation allocation : allocations) {
            paymentAllocationService.unarchiveById(allocation.getId());
        }
    }

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
