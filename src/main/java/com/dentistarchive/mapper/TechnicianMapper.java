package com.dentistarchive.mapper;

import com.dentistarchive.dto.TechnicianDto;
import com.dentistarchive.entity.technician.Technician;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianMapper implements EntityMapper<Technician, TechnicianDto> {

    public TechnicianDto toDto(Technician technician) {

        TechnicianDto dto = new TechnicianDto();

        dto.setId(technician.getId());
        dto.setCreatedAt(technician.getCreatedAt());
        dto.setCreatedBy(technician.getCreatedBy());
        dto.setUpdatedAt(technician.getUpdatedAt());
        dto.setUpdatedBy(technician.getUpdatedBy());
        dto.setArchivedAt(technician.getArchivedAt());
        dto.setArchivedBy(technician.getArchivedBy());
        dto.setArchived(technician.isArchived());

        dto.setName(technician.getName());
        dto.setPhones(technician.getPhones());
        dto.setEmails(technician.getEmails());
        dto.setNotes(technician.getNotes());
        dto.setAddress(technician.getAddress());
        dto.setCollaborationStartDate(technician.getCollaborationStartDate());
        dto.setCollaborationEndDate(technician.getCollaborationEndDate());
        dto.setMonthlyReportStartDate(technician.getMonthlyReportStartDate());

        return dto;
    }

}
