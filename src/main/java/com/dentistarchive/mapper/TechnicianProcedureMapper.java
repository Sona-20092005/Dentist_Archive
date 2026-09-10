package com.dentistarchive.mapper;

import com.dentistarchive.dto.TechnicianProcedureDto;
import com.dentistarchive.entity.technician.TechnicianProcedure;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianProcedureMapper implements EntityMapper<TechnicianProcedure, TechnicianProcedureDto>{

    public TechnicianProcedureDto toDto(TechnicianProcedure procedure) {

        TechnicianProcedureDto dto = new TechnicianProcedureDto();

        dto.setId(procedure.getId());
        dto.setCreatedAt(procedure.getCreatedAt());
        dto.setCreatedBy(procedure.getCreatedBy());
        dto.setUpdatedAt(procedure.getUpdatedAt());
        dto.setUpdatedBy(procedure.getUpdatedBy());
        dto.setArchivedAt(procedure.getArchivedAt());
        dto.setArchivedBy(procedure.getArchivedBy());
        dto.setArchived(procedure.isArchived());
        dto.setName(procedure.getName());
        dto.setPrice(procedure.getPrice());
        dto.setDescription(procedure.getDescription());
        dto.setTechnicianId(procedure.getTechnicianId());

        return dto;
    }
}
