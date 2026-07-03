package com.dentistarchive.service;

import com.dentistarchive.dto.create.WorkScheduleRuleCreateDto;
import com.dentistarchive.dto.update.WorkScheduleRuleCorrectDto;
import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.WorkScheduleRuleRepository;
import com.dentistarchive.search.filter.WorkScheduleRuleFilter;
import com.dentistarchive.service.access.WorkScheduleRuleAccessValidator;
import com.dentistarchive.service.provider.WorkScheduleRuleProvider;
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
    WorkScheduleService workScheduleService;

    public WorkScheduleRuleService(
            WorkScheduleRuleRepository workScheduleRuleRepository,
            WorkScheduleRuleAccessValidator accessValidator,
            WorkScheduleRuleProvider workScheduleRuleProvider,
            WorkScheduleRuleValidator workScheduleRuleValidator,
            WorkScheduleService workScheduleService
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
        this.workScheduleService = workScheduleService;
    }

    @Transactional
    public WorkScheduleRule create(WorkScheduleRuleCreateDto createDto) {
        workScheduleService.getAccessibleWorkSchedule(createDto.getScheduleId());

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

    @Override
    public WorkScheduleRule save(WorkScheduleRule entity) {
        return workScheduleRuleRepository.save(entity);
    }

    @Override
    public void afterArchive(WorkScheduleRule entity) {}

    @Override
    public void afterUnarchive(WorkScheduleRule entity) {}

}
