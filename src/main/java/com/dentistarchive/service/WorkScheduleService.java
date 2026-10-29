package com.dentistarchive.service;

import com.dentistarchive.dto.WorkScheduleModificationImpactDto;
import com.dentistarchive.dto.WorkSchedulePeriodChangePreviewDto;
import com.dentistarchive.dto.create.WorkScheduleCreateDto;
import com.dentistarchive.dto.update.WorkScheduleRevisionCommand;
import com.dentistarchive.dto.update.WorkScheduleRuleRevisionCreateDto;
import com.dentistarchive.dto.update.WorkScheduleUpdateDto;
import com.dentistarchive.entity.schedule.Appointment;
import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.enums.*;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.AppointmentRepository;
import com.dentistarchive.repository.WorkScheduleModificationRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.dentistarchive.utils.ScheduleUtils.isDateWithinPeriod;
import static com.dentistarchive.utils.ScheduleUtils.isPeriodShortened;

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
    AppointmentRepository appointmentRepository;
    AppointmentService appointmentService;
    WorkScheduleRuleService workScheduleRuleService;
    WorkScheduleModificationService workScheduleModificationService;
    private final WorkScheduleModificationRepository workScheduleModificationRepository;

    public WorkScheduleService(
            WorkScheduleRepository workScheduleRepository,
            WorkScheduleAccessValidator accessValidator,
            WorkScheduleProvider workScheduleProvider,
            WorkScheduleValidator workScheduleValidator,
            ClinicService clinicService,
            WorkScheduleRuleProvider workScheduleRuleProvider,
            WorkScheduleRuleValidator workScheduleRuleValidator,
            WorkScheduleRuleRepository workScheduleRuleRepository,
            AppointmentRepository appointmentRepository,
            AppointmentService appointmentService,
            WorkScheduleModificationService workScheduleModificationService,
            WorkScheduleRuleService workScheduleRuleService,
            WorkScheduleModificationRepository workScheduleModificationRepository) {
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
        this.appointmentRepository = appointmentRepository;
        this.appointmentService = appointmentService;
        this.workScheduleModificationService = workScheduleModificationService;
        this.workScheduleRuleService = workScheduleRuleService;
        this.workScheduleModificationRepository = workScheduleModificationRepository;
    }

    @Transactional
    public WorkSchedule create(@Valid WorkScheduleCreateDto createDto) {
        clinicService.getAccessibleClinic(createDto.getClinicId());

        var workSchedule = workScheduleProvider.create(createDto, AuthHolder.getUserId().orElseThrow());
        workScheduleValidator.validate(workSchedule);
        return save(workSchedule);
    }

    @Transactional
    public WorkSchedule update(UUID id, @Valid WorkScheduleUpdateDto updateDto) {
        clinicService.getAccessibleClinic(updateDto.getClinicId());

        WorkSchedule workSchedule = workScheduleRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, id));

        LocalDate oldEffectiveFrom = workSchedule.getEffectiveFrom();
        LocalDate oldEffectiveUntil = workSchedule.getEffectiveUntil();

        accessValidator.validateAccess(workSchedule);
        workScheduleProvider.update(workSchedule, updateDto);
        workScheduleValidator.validate(workSchedule);
        workScheduleRuleValidator.validateUpdate(workSchedule);

        if (isPeriodShortened(oldEffectiveFrom, oldEffectiveUntil, workSchedule.getEffectiveFrom(), workSchedule.getEffectiveUntil())) {
            handleModificationPeriodChange(workSchedule, workSchedule.getEffectiveFrom(), workSchedule.getEffectiveUntil());
        }

        return workScheduleRepository.save(workSchedule);
    }

    @Transactional
    public WorkSchedule revise(UUID scheduleId, @Valid WorkScheduleRevisionCommand command) {
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

    @Transactional(readOnly = true)
    public WorkSchedulePeriodChangePreviewDto previewPeriodChange(UUID id, WorkScheduleUpdateDto updateDto) {
        clinicService.getAccessibleClinic(updateDto.getClinicId());

        WorkSchedule schedule = workScheduleRepository.getByIdAndNotArchived(id).orElseThrow(() ->
                        new EntityNotFoundByIdException(WorkSchedule.class, id));

        accessValidator.validateAccess(schedule);
        workScheduleProvider.update(schedule, updateDto);
        workScheduleValidator.validate(schedule);
        workScheduleRuleValidator.validateUpdate(schedule);

        LocalDate newEffectiveFrom = updateDto.getEffectiveFrom();
        LocalDate newEffectiveUntil = updateDto.getEffectiveUntil();

        List<WorkScheduleModificationImpactDto> affectedModifications = new ArrayList<>();

        List<WorkScheduleModification> modifications = workScheduleModificationRepository.findByScheduleId(schedule.getId());

        for (WorkScheduleModification modification : modifications) {
            ModificationPeriodChangeAction action = getModificationPeriodChangeAction(modification, newEffectiveFrom, newEffectiveUntil);

            if (action == null) {
                continue;
            }

            affectedModifications.add(WorkScheduleModificationImpactDto.builder()
                            .modificationId(modification.getId())
                            .currentType(modification.getModificationType())
                            .action(action)
                            .sourceDate(modification.getSourceDate())
                            .targetDate(modification.getDate())
                            .build()
            );
        }

        return WorkSchedulePeriodChangePreviewDto.builder()
                .confirmationRequired(!affectedModifications.isEmpty())
                .affectedModifications(affectedModifications)
                .build();
    }

    @Override
    public WorkSchedule save(WorkSchedule entity) {
        return workScheduleRepository.save(entity);
    }

    @Transactional
    @Override
    public void archiveById(UUID id) {
        WorkSchedule entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity);
        workScheduleValidator.validateArchive(entity);
        save(entity);
        afterArchive(entity);
    }

    @Transactional
    @Override
    public WorkSchedule unarchiveById(UUID id) {
        WorkSchedule entity = getByIdOrElseThrow(id);
        validateUnarchive(entity);
        unarchive(entity);
        workScheduleValidator.validate(entity);
        entity = save(entity);
        afterUnarchive(entity);
        return entity;
    }

    @Override
    public void afterArchive(WorkSchedule entity) {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        List<Appointment> appointments = appointmentRepository.findFutureActiveAppointments(entity.getId(), today, currentTime);

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentStatus() == AppointmentStatus.SCHEDULED) {
                appointment.setAppointmentSchedulingStatus(AppointmentSchedulingStatus.NEEDS_RESCHEDULING);
                appointmentRepository.save(appointment);
            }
            else {
                appointmentService.archiveById(appointment.getId());
            }
        }

        List<WorkScheduleRule> rules = workScheduleRuleRepository.findByScheduleId(entity.getId());

        for (WorkScheduleRule rule : rules) {
            workScheduleRuleService.archiveById(rule.getId(), WorkScheduleRuleArchiveReason.WORK_SCHEDULE_ARCHIVED);
        }

        List<WorkScheduleModification> modifications = workScheduleModificationRepository.findByScheduleId(entity.getId());

        for (WorkScheduleModification modification : modifications) {
            workScheduleModificationService.archiveById(modification.getId(), WorkScheduleModificationArchiveReason.WORK_SCHEDULE_ARCHIVED);

        }
    }

    @Override
    public void afterUnarchive(WorkSchedule entity) {
        List<WorkScheduleRule> rules = workScheduleRuleRepository.findByScheduleIdAndArchiveReason(entity.getId(), WorkScheduleRuleArchiveReason.WORK_SCHEDULE_ARCHIVED);

        for (WorkScheduleRule rule : rules) {
            workScheduleRuleService.unarchiveById(rule.getId());
        }

        List<WorkScheduleModification> modifications = workScheduleModificationRepository.findByScheduleIdAndArchiveReason(entity.getId(), WorkScheduleModificationArchiveReason.WORK_SCHEDULE_ARCHIVED);

        for (WorkScheduleModification modification : modifications) {
            workScheduleModificationService.unarchiveById(modification.getId());
        }
    }

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

    private void handleModificationPeriodChange(WorkSchedule schedule, LocalDate effectiveFrom, LocalDate effectiveUntil) {
        List<WorkScheduleModification> modifications = workScheduleModificationRepository.findByScheduleId(schedule.getId());

        for (WorkScheduleModification modification : modifications) {
            handleModificationPeriodChange(modification, effectiveFrom, effectiveUntil);
        }
    }

    private void handleModificationPeriodChange(WorkScheduleModification modification, LocalDate effectiveFrom, LocalDate effectiveUntil) {
        ModificationPeriodChangeAction action = getModificationPeriodChangeAction(modification, effectiveFrom, effectiveUntil);

        if (action == null) {
            return;
        }

        switch (action) {
            case ARCHIVE ->
                    workScheduleModificationService.archiveById(modification.getId(), WorkScheduleModificationArchiveReason.WORK_SCHEDULE_PERIOD_CHANGED);

            case CONVERT_TO_ADD ->
                    workScheduleModificationService.convertToAdd(modification);

            case CONVERT_TO_CANCEL ->
                    workScheduleModificationService.convertToCancel(modification);
        }
    }

    private ModificationPeriodChangeAction getModificationPeriodChangeAction(WorkScheduleModification modification, LocalDate effectiveFrom, LocalDate effectiveUntil) {
        if (modification.getModificationType() == ModificationType.ADD) {
            if (!isDateWithinPeriod(modification.getDate(), effectiveFrom, effectiveUntil)) {
                return ModificationPeriodChangeAction.ARCHIVE;
            }
        }
        else if (modification.getModificationType() == ModificationType.CANCEL) {
            if (!isDateWithinPeriod(modification.getSourceDate(), effectiveFrom, effectiveUntil)) {
                return ModificationPeriodChangeAction.ARCHIVE;
            }
        }
        else {
            boolean sourceInside = isDateWithinPeriod(modification.getSourceDate(), effectiveFrom, effectiveUntil);
            boolean targetInside = isDateWithinPeriod(modification.getDate(), effectiveFrom, effectiveUntil);

            if (!sourceInside && !targetInside) {
                return ModificationPeriodChangeAction.ARCHIVE;
            }
            else if (!sourceInside) {
                return ModificationPeriodChangeAction.CONVERT_TO_ADD;
            }
            else if (!targetInside) {
                return ModificationPeriodChangeAction.CONVERT_TO_CANCEL;
            }
        }

        return null;
    }
}
