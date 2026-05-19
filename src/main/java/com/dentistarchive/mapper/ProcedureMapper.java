package com.dentistarchive.mapper;

import com.dentistarchive.dto.ProcedureDto;
import com.dentistarchive.entity.pricelist.Procedure;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcedureMapper implements EntityMapper<Procedure, ProcedureDto>{

    public ProcedureDto toDto(Procedure procedure) {

        ProcedureDto dto = new ProcedureDto();

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
        dto.setDoctorId(procedure.getDoctorId());

        return dto;
    }
}
