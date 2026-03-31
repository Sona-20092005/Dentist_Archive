package com.dentistarchive.service;

import com.dentistarchive.dto.PatientCreateDto;
import com.dentistarchive.entity.patient.Patient;
import com.dentistarchive.repository.PatientRepository;
import com.dentistarchive.search.filter.PatientFilter;
import com.dentistarchive.service.access.PatientAccessValidator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientService extends BaseReadOnlyService<Patient, PatientFilter> {

    PatientRepository patientRepository;
    PatientAccessValidator accessValidator;

    public PatientService(
            PatientRepository patientRepository,
            PatientAccessValidator accessValidator
    ) {
        super(
                Patient.class,
                PatientFilter.class,
                patientRepository,
                accessValidator
        );
        this.patientRepository = patientRepository;
        this.accessValidator = accessValidator;
    }

    public Patient create(PatientCreateDto createDto) {
//        return patientRepository.save(createDto);
        // TODO: 3/31/2026 implement correctly
        return new Patient();
    }
}
