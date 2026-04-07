package com.dentistarchive.mapper;

import com.dentistarchive.dto.PatientDto;
import com.dentistarchive.entity.patient.Patient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientMapper implements EntityMapper<Patient, PatientDto>{


    public PatientDto toDto(Patient patient) {

        PatientDto dto = new PatientDto();

        dto.setId(patient.getId());
        dto.setCreatedAt(patient.getCreatedAt());
        dto.setCreatedBy(patient.getCreatedBy());
        dto.setUpdatedAt(patient.getUpdatedAt());
        dto.setUpdatedBy(patient.getUpdatedBy());
        dto.setArchivedAt(patient.getArchivedAt());
        dto.setArchivedBy(patient.getArchivedBy());
        dto.setArchived(patient.isArchived());
        dto.setName(patient.getName());
        dto.setPhones(patient.getPhones());
        dto.setEmails(patient.getEmails());
        dto.setAddress(patient.getAddress());
        dto.setPassportInformation(patient.getPassportInformation());
        dto.setNotes(patient.getNotes());
        dto.setDoctorId(patient.getDoctorId());

        return dto;
    }
}
