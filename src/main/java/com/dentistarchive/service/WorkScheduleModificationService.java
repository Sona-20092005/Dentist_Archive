package com.dentistarchive.service;

import com.dentistarchive.dto.create.WorkScheduleModificationCreateDto;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.WorkScheduleModificationRepository;
import com.dentistarchive.search.filter.WorkScheduleModificationFilter;
import com.dentistarchive.service.access.WorkScheduleModificationAccessValidator;
import com.dentistarchive.service.provider.WorkScheduleModificationProvider;
import com.dentistarchive.validator.WorkScheduleModificationValidator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
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

        var workScheduleModification = workScheduleModificationProvider.create(createDto);
        workScheduleModificationValidator.validate(workScheduleModification, workSchedule);
        return save(workScheduleModification);
    }

//    @Transactional(propagation = Propagation.NEVER)
//    public WorkScheduleModification update(UUID id, @Valid WorkScheduleModificationUpdateDto updateDto) {
//        clinicService.getAccessibleClinic(updateDto.getClinicId());
//
//        WorkSchedule workSchedule = workScheduleRepository.getByIdAndNotArchived(id)
//                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, id));
//        accessValidator.validateAccess(workSchedule);
//        workScheduleProvider.update(workSchedule, updateDto);
//        workScheduleValidator.validate(workSchedule);
//        workScheduleRuleValidator.validateUpdate(workSchedule);
//        return workScheduleRepository.save(workSchedule);
//    }


    @Override
    public WorkScheduleModification save(WorkScheduleModification entity) {
        return workScheduleModificationRepository.save(entity);
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
