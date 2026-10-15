package com.dentistarchive.service;

import com.dentistarchive.dto.create.PaymentAllocationCreateDto;
import com.dentistarchive.dto.update.PaymentAllocationUpdateDto;
import com.dentistarchive.entity.patient.PaymentAllocation;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.PaymentAllocationRepository;
import com.dentistarchive.search.filter.PaymentAllocationFilter;
import com.dentistarchive.service.access.PaymentAllocationAccessValidator;
import com.dentistarchive.service.provider.PaymentAllocationProvider;
import com.dentistarchive.validator.PaymentAllocationValidator;
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
public class PaymentAllocationService extends BaseReadOnlyService<PaymentAllocation, PaymentAllocationFilter>
        implements ArchivableService<PaymentAllocation, PaymentAllocationFilter> {
    PaymentAllocationRepository paymentAllocationRepository;
    PaymentAllocationAccessValidator accessValidator;
    PaymentAllocationProvider paymentAllocationProvider;
    PaymentAllocationValidator paymentAllocationValidation;
    PaymentService paymentService;
    CompletedTreatmentService completedTreatmentService;
    TreatmentPlanService treatmentPlanService;

    public PaymentAllocationService(
            PaymentAllocationRepository paymentAllocationRepository,
            PaymentAllocationAccessValidator accessValidator,
            PaymentAllocationProvider paymentAllocationProvider,
            PaymentAllocationValidator paymentAllocationValidation,
            PaymentService paymentService,
            CompletedTreatmentService completedTreatmentService,
            TreatmentPlanService treatmentPlanService
    ) {
        super(
                PaymentAllocation.class,
                PaymentAllocationFilter.class,
                paymentAllocationRepository,
                accessValidator
        );
        this.paymentAllocationRepository = paymentAllocationRepository;
        this.accessValidator = accessValidator;
        this.paymentAllocationProvider = paymentAllocationProvider;
        this.paymentAllocationValidation = paymentAllocationValidation;
        this.paymentService = paymentService;
        this.completedTreatmentService = completedTreatmentService;
        this.treatmentPlanService = treatmentPlanService;
    }

    @Transactional
    public PaymentAllocation create(PaymentAllocationCreateDto createDto) {
        var payment = paymentService.getAccessiblePayment(createDto.getPaymentId());

        if (createDto.getTreatmentPlanId() != null) {
            var treatmentPlan = treatmentPlanService
                    .getAccessibleTreatmentPlan(createDto.getTreatmentPlanId());

            if (!payment.getPatientId().equals(treatmentPlan.getPatientId())) {
                throw new IllegalArgumentException(
                        "Payment and treatment plan must belong to the same patient"
                );
            }
        }

        if (createDto.getCompletedTreatmentId() != null) {
            var completedTreatment = completedTreatmentService
                    .getAccessibleCompletedTreatment(createDto.getCompletedTreatmentId());

            if (!payment.getPatientId().equals(completedTreatment.getPatientId())) {
                throw new IllegalArgumentException(
                        "Payment and completed treatment must belong to the same patient"
                );
            }
        }


        var allocation = paymentAllocationProvider.create(createDto);
//        paymentAllocationValidation.validate(allocation);
        return save(allocation);
    }

    @Transactional(propagation = Propagation.NEVER)
    public PaymentAllocation update(UUID id, @Valid PaymentAllocationUpdateDto updateDto) {
        PaymentAllocation allocation = paymentAllocationRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(PaymentAllocation.class, id));

        if(updateDto.getTreatmentPlanId() != null) {
            treatmentPlanService.getAccessibleTreatmentPlan(updateDto.getTreatmentPlanId());
        }
        if(updateDto.getCompletedTreatmentId() != null) {
            completedTreatmentService.getAccessibleCompletedTreatment(updateDto.getCompletedTreatmentId());
        }

        accessValidator.validateAccess(allocation);
        paymentAllocationProvider.update(allocation, updateDto);
        return paymentAllocationRepository.save(allocation);
    }

    @Override
    public PaymentAllocation save(PaymentAllocation entity) {
        return paymentAllocationRepository.save(entity);
    }

    @Override
    public void afterArchive(PaymentAllocation entity) {}

    @Override
    public void afterUnarchive(PaymentAllocation entity) {}

    public PaymentAllocation getAccessiblePaymentAllocation(UUID id) {
        if (id == null) {
            return null;
        }

        PaymentAllocation allocation = paymentAllocationRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(PaymentAllocation.class, id));

        accessValidator.validateAccess(allocation);

        return allocation;
    }

}
