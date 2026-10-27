package com.dentistarchive.service;

import com.dentistarchive.dto.create.PaymentAllocationCreateDto;
import com.dentistarchive.dto.update.PaymentAllocationUpdateDto;
import com.dentistarchive.entity.patient.PaymentAllocation;
import com.dentistarchive.enums.PaymentAllocationArchiveReason;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.PaymentAllocationRepository;
import com.dentistarchive.search.filter.PaymentAllocationFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.PaymentAllocationAccessValidator;
import com.dentistarchive.service.provider.PaymentAllocationProvider;
import com.dentistarchive.utils.ClockUtils;
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
    PaymentLookupService paymentLookupService;
    CompletedTreatmentService completedTreatmentService;
    TreatmentPlanService treatmentPlanService;

    public PaymentAllocationService(
            PaymentAllocationRepository paymentAllocationRepository,
            PaymentAllocationAccessValidator accessValidator,
            PaymentAllocationProvider paymentAllocationProvider,
            PaymentAllocationValidator paymentAllocationValidation,
            PaymentLookupService paymentLookupService,
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
        this.paymentLookupService = paymentLookupService;
        this.completedTreatmentService = completedTreatmentService;
        this.treatmentPlanService = treatmentPlanService;
    }

    @Transactional
    public PaymentAllocation create(PaymentAllocationCreateDto createDto) {
        var payment = paymentLookupService.getAccessiblePayment(createDto.getPaymentId());

        var allocation = paymentAllocationProvider.create(createDto);
        paymentAllocationValidation.validate(allocation, payment);
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
        paymentAllocationValidation.validate(allocation, paymentLookupService.getAccessiblePayment(allocation.getPaymentId()));
        return paymentAllocationRepository.save(allocation);
    }

    @Transactional
    @Override
    public void archiveById(UUID id) {
        PaymentAllocation entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity, PaymentAllocationArchiveReason.USER);
        save(entity);
        afterArchive(entity);
    }

    @Transactional
    public void archiveById(UUID id, PaymentAllocationArchiveReason archiveReason) {
        PaymentAllocation entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity, archiveReason);
        save(entity);
        afterArchive(entity);
    }

    @Transactional
    @Override
    public PaymentAllocation unarchiveById(UUID id) {
        PaymentAllocation entity = getByIdOrElseThrow(id);
        validateUnarchive(entity);
        unarchive(entity);
        paymentAllocationValidation.validateUnarchive(entity, paymentLookupService.getAccessiblePayment(entity.getPaymentId()));
        entity = save(entity);
        afterUnarchive(entity);
        return entity;
    }

    public void archive(PaymentAllocation entity, PaymentAllocationArchiveReason archiveReason) {
        entity.setArchived(true);
        entity.setArchivedAt(ClockUtils.now());
        entity.setArchivedBy(AuthHolder.getUserIdOrElseThrow());
        entity.setArchiveReason(archiveReason);
    }

    @Override
    public void unarchive(PaymentAllocation entity) {
        entity.setArchived(false);
        entity.setArchivedAt(null);
        entity.setArchivedBy(null);
        entity.setArchiveReason(null);
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
