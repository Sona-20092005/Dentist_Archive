package com.dentistarchive.service;

import com.dentistarchive.dto.create.TreatmentPlanItemCreateDto;
import com.dentistarchive.dto.update.TreatmentPlanItemUpdateDto;
import com.dentistarchive.entity.patient.TreatmentPlanItem;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.TreatmentPlanItemRepository;
import com.dentistarchive.search.filter.TreatmentPlanItemFilter;
import com.dentistarchive.service.access.TreatmentPlanItemAccessValidator;
import com.dentistarchive.service.provider.TreatmentPlanItemProvider;
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
public class TreatmentPlanItemService extends BaseReadOnlyService<TreatmentPlanItem, TreatmentPlanItemFilter>
        implements ArchivableService<TreatmentPlanItem, TreatmentPlanItemFilter> {
    TreatmentPlanService planService;
    PatientService patientService;
    ProcedureService procedureService;
    TreatmentPlanItemRepository itemRepository;
    TreatmentPlanItemAccessValidator accessValidator;
    TreatmentPlanItemProvider itemProvider;

    public TreatmentPlanItemService(
            TreatmentPlanService planService,
            PatientService patientService,
            ProcedureService procedureService,
            TreatmentPlanItemRepository itemRepository,
            TreatmentPlanItemAccessValidator accessValidator,
            TreatmentPlanItemProvider itemProvider

    ) {
        super(
                TreatmentPlanItem.class,
                TreatmentPlanItemFilter.class,
                itemRepository,
                accessValidator
        );
        this.planService = planService;
        this.patientService = patientService;
        this.procedureService = procedureService;
        this.itemRepository = itemRepository;
        this.accessValidator = accessValidator;
        this.itemProvider = itemProvider;
    }

    @Transactional
    public TreatmentPlanItem create(@Valid TreatmentPlanItemCreateDto createDto) {
        var plan = planService.getAccessibleTreatmentPlan(createDto.getTreatmentPlanId());
        patientService.getAccessiblePatient(plan.getPatientId());
        procedureService.getAccessibleProcedure(createDto.getProcedureId());

        var item = itemProvider.create(createDto);
        planService.recalculateFinalAmount(item.getTreatmentPlanId());
        return save(item);
    }

    @Transactional(propagation = Propagation.NEVER)
    public TreatmentPlanItem update(UUID id, @Valid TreatmentPlanItemUpdateDto updateDto) {
        procedureService.getAccessibleProcedure(updateDto.getProcedureId());

        TreatmentPlanItem item = itemRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(TreatmentPlanItem.class, id));
        accessValidator.validateAccess(item);

        if(!item.getQuantity().equals(updateDto.getQuantity()) || !item.getUnitPrice().equals(updateDto.getUnitPrice())) {
            planService.recalculateFinalAmount(item.getTreatmentPlanId());
        }

        itemProvider.update(item, updateDto);
        return itemRepository.save(item);
    }

    @Override
    public TreatmentPlanItem save(TreatmentPlanItem entity) {
        return itemRepository.save(entity);
    }

    @Override
    public void afterArchive(TreatmentPlanItem entity) {
        planService.recalculateFinalAmount(entity.getTreatmentPlanId());
    }

    @Override
    public void afterUnarchive(TreatmentPlanItem entity) {
        planService.recalculateFinalAmount(entity.getTreatmentPlanId());
    }

    public TreatmentPlanItem getAccessibleTreatmentPlanItem(UUID id) {
        if (id == null) {
            return null;
        }

        TreatmentPlanItem item = itemRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(TreatmentPlanItem.class, id));

        accessValidator.validateAccess(item);

        return item;
    }

}
