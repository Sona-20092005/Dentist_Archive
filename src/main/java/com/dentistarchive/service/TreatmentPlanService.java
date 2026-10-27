package com.dentistarchive.service;

import com.dentistarchive.dto.create.TreatmentPlanCreateDto;
import com.dentistarchive.dto.update.TreatmentPlanUpdateDto;
import com.dentistarchive.entity.patient.TreatmentPlan;
import com.dentistarchive.entity.patient.TreatmentPlanItem;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.PaymentAllocationRepository;
import com.dentistarchive.repository.TreatmentPlanItemRepository;
import com.dentistarchive.repository.TreatmentPlanRepository;
import com.dentistarchive.search.filter.TreatmentPlanFilter;
import com.dentistarchive.service.access.TreatmentPlanAccessValidator;
import com.dentistarchive.service.provider.TreatmentPlanProvider;
import com.dentistarchive.validator.TreatmentPlanValidator;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanService extends BaseReadOnlyService<TreatmentPlan, TreatmentPlanFilter>
        implements ArchivableService<TreatmentPlan, TreatmentPlanFilter> {
    PatientService patientService;
    TreatmentPlanRepository planRepository;
    TreatmentPlanAccessValidator accessValidator;
    TreatmentPlanProvider planProvider;
    TreatmentPlanItemRepository itemRepository;
    TreatmentPlanValidator planValidator;
    PaymentAllocationRepository paymentAllocationRepository;

    public TreatmentPlanService(
            PatientService patientService,
            TreatmentPlanRepository planRepository,
            TreatmentPlanAccessValidator accessValidator,
            TreatmentPlanProvider planProvider,
            TreatmentPlanItemRepository itemRepository,
            TreatmentPlanValidator planValidator,
            PaymentAllocationRepository paymentAllocationRepository
    ) {
        super(
                TreatmentPlan.class,
                TreatmentPlanFilter.class,
                planRepository,
                accessValidator
        );
        this.patientService = patientService;
        this.planRepository = planRepository;
        this.accessValidator = accessValidator;
        this.planProvider = planProvider;
        this.itemRepository = itemRepository;
        this.planValidator = planValidator;
        this.paymentAllocationRepository = paymentAllocationRepository;
    }

    @Transactional
    public TreatmentPlan create(@Valid TreatmentPlanCreateDto createDto) {
        var patient = patientService.getAccessiblePatient(createDto.getPatientId());

        var plan = planProvider.create(createDto);
        planValidator.validate(plan, calculateSubtotal(plan.getId()));

        plan = planRepository.save(plan);
        patientService.recalculatePatientStatus(patient);
        return plan;
    }

    @Transactional(propagation = Propagation.NEVER)
    public TreatmentPlan update(UUID id, @Valid TreatmentPlanUpdateDto updateDto) {
        TreatmentPlan plan = planRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(TreatmentPlan.class, id));
        var patient = patientService.getAccessiblePatient(plan.getPatientId());
        accessValidator.validateAccess(plan);
        planProvider.update(plan, updateDto);
        planValidator.validate(plan, calculateSubtotal(plan.getId()));

        plan = planRepository.save(plan);
        patientService.recalculatePatientStatus(patient);
        return plan;
    }

    @Override
    public TreatmentPlan save(TreatmentPlan entity) {
        return planRepository.save(entity);
    }

    @Override
    public void archiveById(UUID id) {
        TreatmentPlan entity = getByIdOrElseThrow(id);
        validateArchive(entity);

        if (paymentAllocationRepository.existsByTreatmentPlanIdAndArchivedFalse(id)) {
            throw new IllegalStateException("Cannot archive treatment plan because it has payment allocations");
        }

        archive(entity);
        save(entity);
        afterArchive(entity);
    }

    @Override
    public void afterArchive(TreatmentPlan entity) {
        var patient = patientService.getAccessiblePatient(entity.getPatientId());

        patientService.recalculatePatientStatus(patient);
    }

    @Override
    public void afterUnarchive(TreatmentPlan entity) {
        var patient = patientService.getAccessiblePatient(entity.getPatientId());

        patientService.recalculatePatientStatus(patient);
    }

    public TreatmentPlan getAccessibleTreatmentPlan(UUID id) {
        if (id == null) {
            return null;
        }

        TreatmentPlan plan = planRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(TreatmentPlan.class, id));

        accessValidator.validateAccess(plan);

        return plan;
    }

    @Transactional
    public void recalculateFinalAmount(UUID planId) {
        TreatmentPlan plan = getAccessibleTreatmentPlan(planId);

        BigDecimal subtotal = calculateSubtotal(planId);
        BigDecimal finalAmount = calculateTotal(subtotal, plan.getDiscountPercent(), plan.getDiscountAmount());

        plan.setFinalAmount(finalAmount);
        planRepository.save(plan);
    }

    private BigDecimal calculateTotal(BigDecimal subtotal, BigDecimal discountPercent, BigDecimal discountAmount) {
        BigDecimal total = subtotal;

        if (discountPercent != null) {
            BigDecimal discount = subtotal
                    .multiply(discountPercent)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            total = subtotal.subtract(discount);
        }
        else if (discountAmount != null) {
            total = subtotal.subtract(discountAmount);
        }

        return total;
    }

    private BigDecimal calculateSubtotal(UUID planId) {
        List<TreatmentPlanItem> items = itemRepository.findAllByTreatmentPlanIdAndArchivedFalse(planId);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (TreatmentPlanItem item : items) {
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

            subtotal = subtotal.add(itemTotal);
        }

        return subtotal;
    }

}
