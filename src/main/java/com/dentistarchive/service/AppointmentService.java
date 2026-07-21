package com.dentistarchive.service;

import com.dentistarchive.dto.create.AppointmentCreateDto;
import com.dentistarchive.dto.update.AppointmentUpdateDto;
import com.dentistarchive.entity.schedule.Appointment;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.AppointmentRepository;
import com.dentistarchive.search.filter.AppointmentFilter;
import com.dentistarchive.service.access.AppointmentAccessValidator;
import com.dentistarchive.service.provider.AppointmentProvider;
import com.dentistarchive.validator.AppointmentValidator;
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
public class AppointmentService extends BaseReadOnlyService<Appointment, AppointmentFilter>
        implements ArchivableService<Appointment, AppointmentFilter> {
    AppointmentRepository appointmentRepository;
    AppointmentAccessValidator accessValidator;
    AppointmentProvider appointmentProvider;
    WorkScheduleService workScheduleService;
    AppointmentValidator appointmentValidator;
    PatientService patientService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            AppointmentAccessValidator accessValidator,
            AppointmentProvider appointmentProvider,
            WorkScheduleService workScheduleService,
            AppointmentValidator appointmentValidator,
            PatientService patientService) {
        super(
                Appointment.class,
                AppointmentFilter.class,
                appointmentRepository,
                accessValidator
        );
        this.appointmentRepository = appointmentRepository;
        this.accessValidator = accessValidator;
        this.appointmentProvider = appointmentProvider;
        this.workScheduleService = workScheduleService;
        this.appointmentValidator = appointmentValidator;
        this.patientService = patientService;
    }

    @Transactional
    public Appointment create(AppointmentCreateDto createDto) {
        // TODO: 7/17/2026 add nurse later
        var workSchedule = workScheduleService.getAccessibleWorkSchedule(createDto.getScheduleId());
        patientService.getAccessiblePatient(createDto.getPatientId());

        var appointment = appointmentProvider.create(createDto);
        appointmentValidator.validate(appointment, workSchedule);
        return save(appointment);
    }

    @Transactional(propagation = Propagation.NEVER)
    public Appointment update(UUID id, @Valid AppointmentUpdateDto updateDto) {
        Appointment appointment = appointmentRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Appointment.class, id));

        var workSchedule = workScheduleService.getAccessibleWorkSchedule(updateDto.getScheduleId());
        patientService.getAccessiblePatient(updateDto.getPatientId());

        accessValidator.validateAccess(appointment);
        appointmentProvider.update(appointment, updateDto);
        appointmentValidator.validateUpdate(appointment, workSchedule);
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public Appointment unarchiveById(UUID id) {
        Appointment appointment = getByIdOrElseThrow(id);
        validateUnarchive(appointment);
        var workSchedule = workScheduleService.getAccessibleWorkSchedule(appointment.getScheduleId());
        appointmentValidator.validateUnarchive(appointment, workSchedule);
        unarchive(appointment);
        return save(appointment);
    }

    @Override
    public Appointment save(Appointment entity) {
        return appointmentRepository.save(entity);
    }

    @Override
    public void afterArchive(Appointment entity) {}

    @Override
    public void afterUnarchive(Appointment entity) {}

    public Appointment getAccessibleAppointment(UUID id) {
        if (id == null) {
            return null;
        }

        Appointment appointment = appointmentRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Appointment.class, id));

        accessValidator.validateAccess(appointment);

        return appointment;
    }

}
