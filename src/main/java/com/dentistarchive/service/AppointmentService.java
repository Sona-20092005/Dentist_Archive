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
    WorkScheduleLookupService workScheduleLookupService;
    AppointmentValidator appointmentValidator;
    PatientService patientService;
    NurseService nurseService;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            AppointmentAccessValidator accessValidator,
            AppointmentProvider appointmentProvider,
            WorkScheduleLookupService workScheduleLookupService,
            AppointmentValidator appointmentValidator,
            PatientService patientService,
            NurseService nurseService
    ) {
        super(
                Appointment.class,
                AppointmentFilter.class,
                appointmentRepository,
                accessValidator
        );
        this.appointmentRepository = appointmentRepository;
        this.accessValidator = accessValidator;
        this.appointmentProvider = appointmentProvider;
        this.workScheduleLookupService = workScheduleLookupService;
        this.appointmentValidator = appointmentValidator;
        this.patientService = patientService;
        this.nurseService = nurseService;
    }

    @Transactional
    public Appointment create(@Valid AppointmentCreateDto createDto) {
        nurseService.getAccessibleNurse(createDto.getNurseId());
        var workSchedule = workScheduleLookupService.getAccessibleWorkSchedule(createDto.getScheduleId());
        var patient = patientService.getAccessiblePatient(createDto.getPatientId());
        var appointment = appointmentProvider.create(createDto);
        appointmentValidator.validate(appointment, workSchedule);

        appointment = save(appointment);
        patientService.recalculatePatientStatus(patient);
        return appointment;
    }

    @Transactional(propagation = Propagation.NEVER)
    public Appointment update(UUID id, @Valid AppointmentUpdateDto updateDto) {
        Appointment appointment = appointmentRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Appointment.class, id));

        nurseService.getAccessibleNurse(updateDto.getNurseId());
        var workSchedule = workScheduleLookupService.getAccessibleWorkSchedule(updateDto.getScheduleId());
        var patient = patientService.getAccessiblePatient(updateDto.getPatientId());
        accessValidator.validateAccess(appointment);
        appointmentProvider.update(appointment, updateDto);
        appointmentValidator.validateUpdate(appointment, workSchedule);

        appointment = save(appointment);
        patientService.recalculatePatientStatus(patient);
        return appointment;
    }

    @Override
    @Transactional
    public Appointment unarchiveById(UUID id) {
        Appointment appointment = getByIdOrElseThrow(id);
        validateUnarchive(appointment);
        var workSchedule = workScheduleLookupService.getAccessibleWorkSchedule(appointment.getScheduleId());
        appointmentValidator.validateUnarchive(appointment, workSchedule);
        unarchive(appointment);
        appointment = save(appointment);
        afterUnarchive(appointment);
        return save(appointment);
    }

    @Override
    public Appointment save(Appointment entity) {
        return appointmentRepository.save(entity);
    }

    @Override
    public void afterArchive(Appointment entity) {
        var patient = patientService.getAccessiblePatient(entity.getPatientId());

        patientService.recalculatePatientStatus(patient);
    }

    @Override
    public void afterUnarchive(Appointment entity) {
        var patient = patientService.getAccessiblePatient(entity.getPatientId());

        patientService.recalculatePatientStatus(patient);
    }

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
