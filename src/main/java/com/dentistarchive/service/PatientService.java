package com.dentistarchive.service;

import com.dentistarchive.dto.create.PatientCreateDto;
import com.dentistarchive.dto.update.PatientUpdateDto;
import com.dentistarchive.entity.patient.Patient;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.PatientRepository;
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

import java.util.UUID;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientService extends BaseReadOnlyService<Patient, PatientFilter>
        implements ArchivableService<Patient> {
    PatientRepository patientRepository;
    PatientAccessValidator accessValidator;

    PatientProvider patientProvider;

    public PatientService(
            PatientRepository patientRepository,
            PatientAccessValidator accessValidator,
            PatientProvider patientProvider
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
    }

    @Transactional
    public Patient create(PatientCreateDto createDto) {
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

    @Override
    public Patient save(Patient entity) {
        return patientRepository.save(entity);
    }


//    @Transactional(propagation = Propagation.NEVER)
//    public Patient update(@NotNull UUID id, @NotNull @Valid PatientUpdateDto updateDto) {
//        Patient patient = getByIdOrElseThrow(id);
//        individualBrokerAccessValidator.accessControlBeforeUpdate(patient);
//
//
//        individualBrokerProvider.updateIndividualBroker(patient, updateDto, newAttachmentFileInfos);
//        return individualBrokerRepository.update(patient);
//    }
}
