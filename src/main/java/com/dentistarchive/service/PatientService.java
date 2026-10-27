package com.dentistarchive.service;

import com.dentistarchive.dto.create.PatientCreateDto;
import com.dentistarchive.dto.update.PatientUpdateDto;
import com.dentistarchive.entity.patient.Patient;
import com.dentistarchive.enums.AppointmentSchedulingStatus;
import com.dentistarchive.enums.AppointmentStatus;
import com.dentistarchive.enums.PatientStatus;
import com.dentistarchive.enums.TreatmentPlanStatus;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.exception.PatientAlreadyInactiveException;
import com.dentistarchive.exception.PatientNotInactiveException;
import com.dentistarchive.repository.AppointmentRepository;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.repository.TreatmentPlanRepository;
import com.dentistarchive.search.filter.PatientFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.PatientAccessValidator;
import com.dentistarchive.service.provider.PatientProvider;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientService extends BaseReadOnlyService<Patient, PatientFilter>
        implements ArchivableService<Patient, PatientFilter> {
    PatientRepository patientRepository;
    PatientAccessValidator accessValidator;
    PatientProvider patientProvider;
    AppointmentRepository appointmentRepository;
    TreatmentPlanRepository treatmentPlanRepository;


    public PatientService(
            PatientRepository patientRepository,
            PatientAccessValidator accessValidator,
            PatientProvider patientProvider,
            AppointmentRepository appointmentRepository,
            TreatmentPlanRepository treatmentPlanRepository
    ) {
        super(
                Patient.class,
                PatientFilter.class,
                patientRepository,
                accessValidator
        );
        this.patientRepository = patientRepository;
        this.accessValidator = accessValidator;
        this.patientProvider = patientProvider;
        this.treatmentPlanRepository = treatmentPlanRepository;
        this.appointmentRepository = appointmentRepository;
    }

    // TODO: 10/20/2026 come back after reviewing patient and scheduling and take care of patient status

    @Transactional
    public Patient create(@Valid PatientCreateDto createDto) {

        var patient = patientProvider.create(createDto, AuthHolder.getUserId().orElseThrow());
        return save(patient);
    }

    @Transactional(propagation = Propagation.NEVER)
    public Patient update(UUID id, @Valid PatientUpdateDto updateDto) {
        Patient patient = patientRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Patient.class, id));
        accessValidator.validateAccess(patient);
        patientProvider.update(patient, updateDto);
        return patientRepository.save(patient);
    }

    public Patient deactivate(UUID id) {
        Patient patient = getByIdOrElseThrow(id);
        getAccessValidator().validateAccess(patient);
        if (patient.getPatientStatus() == PatientStatus.INACTIVE) {
            throw new PatientAlreadyInactiveException(patient.getId());
        }
        updatePatientStatus(patient, PatientStatus.INACTIVE);
        return save(patient);
    }

    public Patient reactivate(UUID id) {
        Patient patient = getByIdOrElseThrow(id);
        getAccessValidator().validateAccess(patient);
        if (patient.getPatientStatus() != PatientStatus.INACTIVE) {
            throw new PatientNotInactiveException(patient.getId());
        }
        updatePatientStatus(patient, PatientStatus.ACTIVE);
        return save(patient);
    }


    @Override
    public Patient save(Patient entity) {
        return patientRepository.save(entity);
    }

    @Override
    public void afterArchive(Patient entity) {}

    @Override
    public void afterUnarchive(Patient entity) {}

    public void recalculatePatientStatus(Patient patient) {
        UUID patientId = patient.getId();

        PatientStatus newStatus;

        if (hasFutureAppointment(patientId)) {
            newStatus = PatientStatus.WAITING_FOR_APPOINTMENT;
        }
        else if (hasOngoingTreatment(patientId)) {
            newStatus = PatientStatus.IN_TREATMENT;
        }
        else {
            newStatus = PatientStatus.ACTIVE;
        }

        updatePatientStatus(patient, newStatus);
    }

    public void updatePatientStatus(Patient patient, PatientStatus patientStatus) {
        patient.setPatientStatus(patientStatus);
        patient.setPatientStatusSortOrder(patientStatus.getSortOrder());
    }

    public boolean hasFutureAppointment(UUID patientId) {
        return appointmentRepository.hasFutureAppointments(patientId, LocalDate.now(), AppointmentSchedulingStatus.NORMAL, AppointmentStatus.SCHEDULED);
    }

    private boolean hasOngoingTreatment(UUID patientId) {
        return treatmentPlanRepository.existsByPatientIdAndPlanStatusAndArchivedFalse(patientId, TreatmentPlanStatus.ACTIVE);
    }

    public Patient getAccessiblePatient(UUID id) {
        if (id == null) {
            return null;
        }

        Patient patient = patientRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Patient.class, id));

        accessValidator.validateAccess(patient);

        return patient;
    }

}
