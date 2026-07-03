package com.dentistarchive.mapper;

import com.dentistarchive.dto.ClinicDto;
import com.dentistarchive.entity.Clinic;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClinicMapper implements EntityMapper<Clinic, ClinicDto>{

    public ClinicDto toDto(Clinic clinic) {

        ClinicDto dto = new ClinicDto();

        dto.setId(clinic.getId());
        dto.setCreatedAt(clinic.getCreatedAt());
        dto.setCreatedBy(clinic.getCreatedBy());
        dto.setUpdatedAt(clinic.getUpdatedAt());
        dto.setUpdatedBy(clinic.getUpdatedBy());
        dto.setArchivedAt(clinic.getArchivedAt());
        dto.setArchivedBy(clinic.getArchivedBy());
        dto.setArchived(clinic.isArchived());
        dto.setName(clinic.getName());
        dto.setPhones(clinic.getPhones());
        dto.setEmails(clinic.getEmails());
        dto.setAddress(clinic.getAddress());
        dto.setNotes(clinic.getNotes());
        dto.setDoctorId(clinic.getDoctorId());

        return dto;
    }
}
