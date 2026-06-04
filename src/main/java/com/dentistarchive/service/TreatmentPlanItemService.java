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
    TreatmentPlanItemRepository itemRepository;
    TreatmentPlanItemAccessValidator accessValidator;
    TreatmentPlanItemProvider itemProvider;

    public TreatmentPlanItemService(
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
        this.itemRepository = itemRepository;
        this.accessValidator = accessValidator;
        this.itemProvider = itemProvider;
    }

    @Transactional
    public TreatmentPlanItem create(TreatmentPlanItemCreateDto createDto) {
        var item = itemProvider.create(createDto);
        return save(item);
    }

    @Transactional(propagation = Propagation.NEVER)
    public TreatmentPlanItem update(UUID id, @Valid TreatmentPlanItemUpdateDto updateDto) {
        TreatmentPlanItem item = itemRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(TreatmentPlanItem.class, id));
        accessValidator.validateAccess(item);
        itemProvider.update(item, updateDto);
        return itemRepository.save(item);
    }

    @Override
    public TreatmentPlanItem save(TreatmentPlanItem entity) {
        return itemRepository.save(entity);
    }

    @Override
    public void afterArchive(TreatmentPlanItem entity) {}

    @Override
    public void afterUnarchive(TreatmentPlanItem entity) {}

}
