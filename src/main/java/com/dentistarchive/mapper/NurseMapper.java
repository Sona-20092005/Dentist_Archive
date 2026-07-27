package com.dentistarchive.mapper;

import com.dentistarchive.dto.NurseDto;
import com.dentistarchive.entity.nurse.Nurse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseMapper implements EntityMapper<Nurse, NurseDto> {

    public NurseDto toDto(Nurse nurse) {

        NurseDto dto = new NurseDto();

        dto.setId(nurse.getId());
        dto.setCreatedAt(nurse.getCreatedAt());
        dto.setCreatedBy(nurse.getCreatedBy());
        dto.setUpdatedAt(nurse.getUpdatedAt());
        dto.setUpdatedBy(nurse.getUpdatedBy());
        dto.setArchivedAt(nurse.getArchivedAt());
        dto.setArchivedBy(nurse.getArchivedBy());
        dto.setArchived(nurse.isArchived());
        dto.setName(nurse.getName());
        dto.setPhones(nurse.getPhones());
        dto.setEmails(nurse.getEmails());
        dto.setNotes(nurse.getNotes());
        dto.setBaseSalary(nurse.getBaseSalary());
        dto.setHourlyRate(nurse.getHourlyRate());
        dto.setPayrollStartDate(nurse.getPayrollStartDate());
        dto.setHireDate(nurse.getHireDate());
        dto.setTerminationDate(nurse.getTerminationDate());
        dto.setClinicId(nurse.getClinicId());

        return dto;
    }

}
