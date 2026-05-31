package com.dentistarchive.service;

import com.dentistarchive.dto.create.TreatmentPlanCreateDto;
import com.dentistarchive.dto.update.TreatmentPlanUpdateDto;
import com.dentistarchive.entity.patient.TreatmentPlan;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.TreatmentPlanRepository;
import com.dentistarchive.search.filter.TreatmentPlanFilter;
import com.dentistarchive.service.access.TreatmentPlanAccessValidator;
import com.dentistarchive.service.provider.TreatmentPlanProvider;
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
public class TreatmentPlanService extends BaseReadOnlyService<TreatmentPlan, TreatmentPlanFilter>
        implements ArchivableService<TreatmentPlan, TreatmentPlanFilter> {
    TreatmentPlanRepository planRepository;
    TreatmentPlanAccessValidator accessValidator;
    TreatmentPlanProvider planProvider;

    public TreatmentPlanService(
            TreatmentPlanRepository planRepository,
            TreatmentPlanAccessValidator accessValidator,
            TreatmentPlanProvider planProvider

    ) {
        super(
                TreatmentPlan.class,
                TreatmentPlanFilter.class,
                planRepository,
                accessValidator
        );
        this.planRepository = planRepository;
        this.accessValidator = accessValidator;
        this.planProvider = planProvider;
    }

    @Transactional
    public TreatmentPlan create(TreatmentPlanCreateDto createDto) {
        var plan = planProvider.create(createDto);
        return save(plan);
    }

    @Transactional(propagation = Propagation.NEVER)
    public TreatmentPlan update(UUID id, @Valid TreatmentPlanUpdateDto updateDto) {
        TreatmentPlan plan = planRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(TreatmentPlan.class, id));
        accessValidator.validateAccess(plan);
        planProvider.update(plan, updateDto);
        return planRepository.save(plan);
    }

    @Override
    public TreatmentPlan save(TreatmentPlan entity) {
        return planRepository.save(entity);
    }

    @Override
    public void afterArchive(TreatmentPlan entity) {}

    @Override
    public void afterUnarchive(TreatmentPlan entity) {}

}
