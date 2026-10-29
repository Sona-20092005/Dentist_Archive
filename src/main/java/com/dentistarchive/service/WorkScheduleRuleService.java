package com.dentistarchive.service;

import com.dentistarchive.dto.create.WorkScheduleRuleCreateDto;
import com.dentistarchive.dto.update.WorkScheduleRuleCorrectDto;
import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.enums.WorkScheduleRuleArchiveReason;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.WorkScheduleRuleRepository;
import com.dentistarchive.search.filter.WorkScheduleRuleFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.WorkScheduleRuleAccessValidator;
import com.dentistarchive.service.provider.WorkScheduleRuleProvider;
import com.dentistarchive.utils.ClockUtils;
import com.dentistarchive.validator.WorkScheduleRuleValidator;
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
public class WorkScheduleRuleService extends BaseReadOnlyService<WorkScheduleRule, WorkScheduleRuleFilter>
        implements ArchivableService<WorkScheduleRule, WorkScheduleRuleFilter> {
    WorkScheduleRuleRepository workScheduleRuleRepository;
    WorkScheduleRuleAccessValidator accessValidator;
    WorkScheduleRuleProvider workScheduleRuleProvider;
    WorkScheduleRuleValidator workScheduleRuleValidator;
    WorkScheduleLookupService WorkScheduleLookupService;

    public WorkScheduleRuleService(
            WorkScheduleRuleRepository workScheduleRuleRepository,
            WorkScheduleRuleAccessValidator accessValidator,
            WorkScheduleRuleProvider workScheduleRuleProvider,
            WorkScheduleRuleValidator workScheduleRuleValidator,
            WorkScheduleLookupService WorkScheduleLookupService
    ) {
        super(
                WorkScheduleRule.class,
                WorkScheduleRuleFilter.class,
                workScheduleRuleRepository,
                accessValidator
        );
        this.workScheduleRuleRepository = workScheduleRuleRepository;
        this.accessValidator = accessValidator;
        this.workScheduleRuleProvider = workScheduleRuleProvider;
        this.workScheduleRuleValidator = workScheduleRuleValidator;
        this.WorkScheduleLookupService = WorkScheduleLookupService;
    }

    @Transactional
    public WorkScheduleRule create(@Valid WorkScheduleRuleCreateDto createDto) {
        WorkScheduleLookupService.getAccessibleWorkSchedule(createDto.getScheduleId());

        var workSchedule = workScheduleRuleProvider.create(createDto);
        workScheduleRuleValidator.validate(workSchedule);
        return save(workSchedule);
    }

    @Transactional(propagation = Propagation.NEVER)
    public WorkScheduleRule correct(UUID id, @Valid WorkScheduleRuleCorrectDto correctDto) {
        WorkScheduleRule workScheduleRule = workScheduleRuleRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkScheduleRule.class, id));

        accessValidator.validateAccess(workScheduleRule);
        workScheduleRuleProvider.update(workScheduleRule, correctDto);
        workScheduleRuleValidator.validate(workScheduleRule);
        return workScheduleRuleRepository.save(workScheduleRule);
    }

    @Transactional
    @Override
    public void archiveById(UUID id) {
        WorkScheduleRule entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity, WorkScheduleRuleArchiveReason.USER);
        save(entity);
        afterArchive(entity);
    }

    @Transactional
    public void archiveById(UUID id, WorkScheduleRuleArchiveReason archiveReason) {
        WorkScheduleRule entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity, archiveReason);
        save(entity);
        afterArchive(entity);
    }

    public void archive(WorkScheduleRule entity, WorkScheduleRuleArchiveReason archiveReason) {
        entity.setArchived(true);
        entity.setArchivedAt(ClockUtils.now());
        entity.setArchivedBy(AuthHolder.getUserIdOrElseThrow());
        entity.setArchiveReason(archiveReason);
    }

    @Override
    public void unarchive(WorkScheduleRule entity) {
        entity.setArchived(false);
        entity.setArchivedAt(null);
        entity.setArchivedBy(null);
        entity.setArchiveReason(null);
    }

    @Override
    public WorkScheduleRule save(WorkScheduleRule entity) {
        return workScheduleRuleRepository.save(entity);
    }

    @Override
    public void afterArchive(WorkScheduleRule entity) {}

    @Override
    public void afterUnarchive(WorkScheduleRule entity) {}

}
