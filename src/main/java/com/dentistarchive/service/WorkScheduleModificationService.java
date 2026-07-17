package com.dentistarchive.service;

import com.dentistarchive.dto.create.WorkScheduleModificationCreateDto;
import com.dentistarchive.dto.update.WorkScheduleModificationUpdateDto;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.WorkScheduleModificationRepository;
import com.dentistarchive.search.filter.WorkScheduleModificationFilter;
import com.dentistarchive.service.access.WorkScheduleModificationAccessValidator;
import com.dentistarchive.service.provider.WorkScheduleModificationProvider;
import com.dentistarchive.validator.WorkScheduleModificationValidator;
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
public class WorkScheduleModificationService extends BaseReadOnlyService<WorkScheduleModification, WorkScheduleModificationFilter>
        implements ArchivableService<WorkScheduleModification, WorkScheduleModificationFilter> {
    WorkScheduleModificationRepository workScheduleModificationRepository;
    WorkScheduleModificationAccessValidator accessValidator;
    WorkScheduleModificationProvider workScheduleModificationProvider;
    WorkScheduleModificationValidator workScheduleModificationValidator;
    WorkScheduleService workScheduleService;

    public WorkScheduleModificationService(
            WorkScheduleModificationRepository workScheduleModificationRepository,
            WorkScheduleModificationAccessValidator accessValidator,
            WorkScheduleModificationProvider workScheduleModificationProvider,
            WorkScheduleService workScheduleService,
            WorkScheduleModificationValidator workScheduleModificationValidator
    ) {
        super(
                WorkScheduleModification.class,
                WorkScheduleModificationFilter.class,
                workScheduleModificationRepository,
                accessValidator
        );
        this.workScheduleModificationRepository = workScheduleModificationRepository;
        this.accessValidator = accessValidator;
        this.workScheduleModificationProvider = workScheduleModificationProvider;
        this.workScheduleService = workScheduleService;
        this.workScheduleModificationValidator = workScheduleModificationValidator;
    }
    // TODO: 7/3/2026 finish the service and validation as soon as the calendar is done

    @Transactional
    public WorkScheduleModification create(WorkScheduleModificationCreateDto createDto) {
        var workSchedule = workScheduleService.getAccessibleWorkSchedule(createDto.getScheduleId());

        var modification = workScheduleModificationProvider.create(createDto);
        workScheduleModificationValidator.validate(modification, workSchedule);
        return save(modification);
    }

    @Transactional(propagation = Propagation.NEVER)
    public WorkScheduleModification update(UUID id, @Valid WorkScheduleModificationUpdateDto updateDto) {
        WorkScheduleModification modification = workScheduleModificationRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkScheduleModification.class, id));
        accessValidator.validateAccess(modification);
        workScheduleModificationValidator.validateUpdatable(modification);
        var workSchedule = workScheduleService.getAccessibleWorkSchedule(updateDto.getScheduleId());

        UUID previousScheduleId = modification.getScheduleId();
        workScheduleModificationProvider.update(modification, updateDto);
        workScheduleModificationValidator.validateUpdate(modification, previousScheduleId, workSchedule);
        return workScheduleModificationRepository.save(modification);
    }


    @Override
    public WorkScheduleModification save(WorkScheduleModification entity) {
        return workScheduleModificationRepository.save(entity);
    }

    @Override
    @Transactional
    public WorkScheduleModification unarchiveById(UUID id) {
        WorkScheduleModification modification = getByIdOrElseThrow(id);
        validateUnarchive(modification);
        var workSchedule = workScheduleService.getAccessibleWorkSchedule(modification.getScheduleId());
        workScheduleModificationValidator.validateUnarchive(modification, workSchedule);
        unarchive(modification);
        modification = save(modification);
        return modification;
    }

    @Override
    public void afterArchive(WorkScheduleModification entity) {}

    @Override
    public void afterUnarchive(WorkScheduleModification entity) {}

    public WorkScheduleModification getAccessibleWorkSchedule(UUID id) {
        if (id == null) {
            return null;
        }

        WorkScheduleModification workScheduleModification = workScheduleModificationRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(WorkScheduleModification.class, id));

        accessValidator.validateAccess(workScheduleModification);

        return workScheduleModification;
    }


    // TODO: 7/3/2026 think also about the session type in modification



}
