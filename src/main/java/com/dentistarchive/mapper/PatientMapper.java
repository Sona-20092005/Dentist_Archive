package com.dentistarchive.mapper;

import com.dentistarchive.dto.PatientDto;
import com.dentistarchive.entity.Patient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

// TODO: 3/26/2026 extend from EntityMapper 
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientMapper {


    public PatientDto toDto(Patient patient) {
        // TODO: 3/26/2026 change to real mapper logic 
        return new PatientDto();
    }
}
