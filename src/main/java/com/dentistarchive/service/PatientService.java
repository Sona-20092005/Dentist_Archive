package com.dentistarchive.service;

import com.dentistarchive.entity.patient.Patient;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

// TODO: 3/26/2026 extend from BaseReadOnlyService
@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientService {


    public Patient getByIdOrElseThrow(UUID id) {
        // TODO: 3/26/2026 change with real service logic
        return new Patient();
    }
}
