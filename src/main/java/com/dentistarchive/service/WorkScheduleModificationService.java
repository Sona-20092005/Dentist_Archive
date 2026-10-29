package com.dentistarchive.service;

import com.dentistarchive.dto.create.WorkScheduleModificationCreateDto;
import com.dentistarchive.dto.update.WorkScheduleModificationUpdateDto;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.enums.ModificationType;
import com.dentistarchive.enums.WorkScheduleModificationArchiveReason;
import com.dentistarchive.enums.WorkSessionType;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.WorkScheduleModificationRepository;
import com.dentistarchive.search.filter.WorkScheduleModificationFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.WorkScheduleModificationAccessValidator;
import com.dentistarchive.service.provider.WorkScheduleModificationProvider;
import com.dentistarchive.utils.ClockUtils;
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
    WorkScheduleLookupService workScheduleLookupService;
    RentOvertimeSynchronizer rentOvertimeSynchronizer;
    
    // TODO: 10/29/2026 after looking at rent syncronizer, uncomment the apply changes

    public WorkScheduleModificationService(
            WorkScheduleModificationRepository workScheduleModificationRepository,
            WorkScheduleModificationAccessValidator accessValidator,
            WorkScheduleModificationProvider workScheduleModificationProvider,
            WorkScheduleLookupService workScheduleLookupService,
            WorkScheduleModificationValidator workScheduleModificationValidator,
            RentOvertimeSynchronizer rentOvertimeSynchronizer
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
        this.workScheduleLookupService = workScheduleLookupService;
        this.workScheduleModificationValidator = workScheduleModificationValidator;
        this.rentOvertimeSynchronizer = rentOvertimeSynchronizer;
    }

    // TODO: 7/3/2026 finish the service and validation as soon as the calendar is done

    @Transactional
    public WorkScheduleModification create(@Valid WorkScheduleModificationCreateDto createDto) {
        var workSchedule = workScheduleLookupService.getAccessibleWorkSchedule(createDto.getScheduleId());

        var modification = workScheduleModificationProvider.create(createDto);
        workScheduleModificationValidator.validate(modification, workSchedule);

        modification = save(modification);

//        rentOvertimeSynchronizer.applyModificationCreate(modification);

        return modification;
    }

    @Transactional(propagation = Propagation.NEVER)
    public WorkScheduleModification update(UUID id, @Valid WorkScheduleModificationUpdateDto updateDto) {
        WorkScheduleModification modification = workScheduleModificationRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkScheduleModification.class, id));
        accessValidator.validateAccess(modification);

        WorkSessionType orginalType = modification.getWorkSessionType();

        workScheduleModificationValidator.validateUpdatable(modification);
        var workSchedule = workScheduleLookupService.getAccessibleWorkSchedule(updateDto.getScheduleId());

        UUID previousScheduleId = modification.getScheduleId();
        workScheduleModificationProvider.update(modification, updateDto);
        workScheduleModificationValidator.validateUpdate(modification, previousScheduleId, workSchedule);

//        if(orginalType == WorkSessionType.OVERTIME && modification.getWorkSessionType() == WorkSessionType.REGULAR) {
//            rentOvertimeSynchronizer.applyModificationDelete(modification);
//        }
//        else if(orginalType == WorkSessionType.REGULAR && modification.getWorkSessionType() == WorkSessionType.OVERTIME) {
//            rentOvertimeSynchronizer.applyModificationCreate(modification);
//        }

        return workScheduleModificationRepository.save(modification);
    }

    @Override
    public WorkScheduleModification save(WorkScheduleModification entity) {
        return workScheduleModificationRepository.save(entity);
    }

    @Transactional
    @Override
    public void archiveById(UUID id) {
        WorkScheduleModification entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity, WorkScheduleModificationArchiveReason.USER);
        save(entity);
        afterArchive(entity);
    }

    @Transactional
    public void archiveById(UUID id, WorkScheduleModificationArchiveReason archiveReason) {
        WorkScheduleModification entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity, archiveReason);
        save(entity);
        afterArchive(entity);
    }

    @Override
    @Transactional
    public WorkScheduleModification unarchiveById(UUID id) {
        WorkScheduleModification modification = getByIdOrElseThrow(id);
        validateUnarchive(modification);
        var workSchedule = workScheduleLookupService.getAccessibleWorkSchedule(modification.getScheduleId());
        workScheduleModificationValidator.validateUnarchive(modification, workSchedule);
        unarchive(modification);
        modification = save(modification);
        return modification;
    }

    public void archive(WorkScheduleModification entity, WorkScheduleModificationArchiveReason archiveReason) {
        entity.setArchived(true);
        entity.setArchivedAt(ClockUtils.now());
        entity.setArchivedBy(AuthHolder.getUserIdOrElseThrow());
        entity.setArchiveReason(archiveReason);
    }

    @Override
    public void unarchive(WorkScheduleModification entity) {
        entity.setArchived(false);
        entity.setArchivedAt(null);
        entity.setArchivedBy(null);
        entity.setArchiveReason(null);
    }

    @Override
    public void afterArchive(WorkScheduleModification entity) {
//        rentOvertimeSynchronizer.applyModificationDelete(entity);
    }

    @Override
    public void afterUnarchive(WorkScheduleModification entity) {
//        rentOvertimeSynchronizer.applyModificationCreate(entity);
    }

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

    public void convertToAdd(WorkScheduleModification modification) {
        modification.setModificationType(ModificationType.ADD);
        modification.setSourceDate(null);
        modification.setSourceStartTime(null);
        modification.setSourceEndTime(null);

        workScheduleModificationRepository.save(modification);
    }

    public void convertToCancel(WorkScheduleModification modification) {
        modification.setModificationType(ModificationType.CANCEL);

        modification.setDate(null);
        modification.setStartTime(null);
        modification.setEndTime(null);

        workScheduleModificationRepository.save(modification);
    }

    // TODO: 7/3/2026 think also about the session type in modification

}
