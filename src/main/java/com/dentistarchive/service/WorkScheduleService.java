package com.dentistarchive.service;

import com.dentistarchive.dto.create.WorkScheduleCreateDto;
import com.dentistarchive.dto.update.WorkScheduleRevisionCommand;
import com.dentistarchive.dto.update.WorkScheduleRuleRevisionCreateDto;
import com.dentistarchive.dto.update.WorkScheduleUpdateDto;
import com.dentistarchive.entity.schedule.Appointment;
import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.entity.schedule.WorkScheduleRule;
import com.dentistarchive.enums.AppointmentSchedulingStatus;
import com.dentistarchive.enums.AppointmentStatus;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.AppointmentRepository;
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
import java.time.LocalTime;
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
    AppointmentRepository appointmentRepository;
    AppointmentService appointmentService;

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
            AppointmentService appointmentService
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
        this.appointmentRepository = appointmentRepository;
        this.appointmentService = appointmentService;
    }

    @Transactional
    public WorkSchedule create(@Valid WorkScheduleCreateDto createDto) {
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

    @Override
    public WorkSchedule save(WorkSchedule entity) {
        return workScheduleRepository.save(entity);
    }

    @Override
    public void archiveById(UUID id) {
        WorkSchedule entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity);
        workScheduleValidator.validateArchive(entity);
        save(entity);
        afterArchive(entity);
    }

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
    }

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
