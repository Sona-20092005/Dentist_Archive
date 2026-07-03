package com.dentistarchive.service;

import com.dentistarchive.dto.create.WorkScheduleCreateDto;
import com.dentistarchive.dto.update.WorkScheduleRevisionCommand;
import com.dentistarchive.dto.update.WorkScheduleRuleRevisionCreateDto;
import com.dentistarchive.dto.update.WorkScheduleUpdateDto;
import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.WorkScheduleRepository;
import com.dentistarchive.repository.WorkScheduleRuleRepository;
import com.dentistarchive.search.filter.WorkScheduleFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.WorkScheduleAccessValidator;
import com.dentistarchive.service.provider.WorkScheduleProvider;
import com.dentistarchive.service.provider.WorkScheduleRuleProvider;
import com.dentistarchive.validator.WorkScheduleRuleValidator;
import com.dentistarchive.validator.WorkScheduleValidator;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.dentistarchive.utils.ScheduleUtils.isDateWithinPeriod;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleService extends BaseReadOnlyService<WorkSchedule, WorkScheduleFilter>
        implements ArchivableService<WorkSchedule, WorkScheduleFilter> {
    WorkScheduleRepository workScheduleRepository;
    WorkScheduleAccessValidator accessValidator;
    WorkScheduleProvider workScheduleProvider;
    WorkScheduleValidator workScheduleValidator;
    ClinicService clinicService;
    WorkScheduleRuleProvider workScheduleRuleProvider;
    WorkScheduleRuleValidator workScheduleRuleValidator;
    WorkScheduleRuleRepository workScheduleRuleRepository;

    public WorkScheduleService(
            WorkScheduleRepository workScheduleRepository,
            WorkScheduleAccessValidator accessValidator,
            WorkScheduleProvider workScheduleProvider,
            WorkScheduleValidator workScheduleValidator,
            ClinicService clinicService,
            WorkScheduleRuleProvider workScheduleRuleProvider,
            WorkScheduleRuleValidator workScheduleRuleValidator,
            WorkScheduleRuleRepository workScheduleRuleRepository
    ) {
        super(
                WorkSchedule.class,
                WorkScheduleFilter.class,
                workScheduleRepository,
                accessValidator
        );
        this.workScheduleRepository = workScheduleRepository;
        this.accessValidator = accessValidator;
        this.workScheduleProvider = workScheduleProvider;
        this.workScheduleValidator = workScheduleValidator;
        this.clinicService = clinicService;
        this.workScheduleRuleProvider = workScheduleRuleProvider;
        this.workScheduleRuleValidator = workScheduleRuleValidator;
        this.workScheduleRuleRepository = workScheduleRuleRepository;
    }

    @Transactional
    public WorkSchedule create(WorkScheduleCreateDto createDto) {
        clinicService.getAccessibleClinic(createDto.getClinicId());

        var workSchedule = workScheduleProvider.create(createDto, AuthHolder.getUserId().orElseThrow());
        workScheduleValidator.validate(workSchedule);
        return save(workSchedule);
    }

    @Transactional(propagation = Propagation.NEVER)
    public WorkSchedule update(UUID id, @Valid WorkScheduleUpdateDto updateDto) {
        clinicService.getAccessibleClinic(updateDto.getClinicId());

        WorkSchedule workSchedule = workScheduleRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, id));
        accessValidator.validateAccess(workSchedule);
        workScheduleProvider.update(workSchedule, updateDto);
        workScheduleValidator.validate(workSchedule);
        workScheduleRuleValidator.validateUpdate(workSchedule);
        return workScheduleRepository.save(workSchedule);
    }

    @Transactional
    public WorkSchedule revise(UUID scheduleId, WorkScheduleRevisionCommand command) {
        WorkSchedule original = getAccessibleWorkSchedule(scheduleId);

        if (!isDateWithinPeriod(command.getEffectiveFrom(), original.getEffectiveFrom(), original.getEffectiveUntil())) {
            throw new IllegalArgumentException("Invalid effective date");
        }

        LocalDate originalEffectiveUntil = original.getEffectiveUntil();
        original.setEffectiveUntil(command.getEffectiveFrom().minusDays(1));
        workScheduleRepository.save(original);
        workScheduleRepository.flush();

        WorkSchedule revised = workScheduleProvider.createRevision(original, command.getEffectiveFrom(), originalEffectiveUntil);
        revised = workScheduleRepository.save(revised);

        List<WorkScheduleRule> rules = new ArrayList<>();

        for (WorkScheduleRuleRevisionCreateDto ruleCreateDto : command.getRules()) {
            rules.add(workScheduleRuleProvider.create(ruleCreateDto, revised.getId()));
        }

        workScheduleRuleValidator.validateRevision(rules, revised);

        workScheduleRuleRepository.saveAll(rules);

        return revised;
    }

    @Override
    public WorkSchedule save(WorkSchedule entity) {
        return workScheduleRepository.save(entity);
    }

    @Override
    public void afterArchive(WorkSchedule entity) {}

    @Override
    public void afterUnarchive(WorkSchedule entity) {}

    public WorkSchedule getAccessibleWorkSchedule(UUID id) {
        if (id == null) {
            return null;
        }

        WorkSchedule workSchedule = workScheduleRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(WorkSchedule.class, id));

        accessValidator.validateAccess(workSchedule);

        return workSchedule;
    }



}
