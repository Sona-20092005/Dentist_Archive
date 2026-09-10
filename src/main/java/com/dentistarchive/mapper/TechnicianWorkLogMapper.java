package com.dentistarchive.mapper;

import com.dentistarchive.dto.TechnicianWorkLogDto;
import com.dentistarchive.entity.technician.TechnicianWorkLog;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianWorkLogMapper implements EntityMapper<TechnicianWorkLog, TechnicianWorkLogDto>{

    public TechnicianWorkLogDto toDto(TechnicianWorkLog workLog) {

        TechnicianWorkLogDto dto = new TechnicianWorkLogDto();

        dto.setId(workLog.getId());
        dto.setCreatedAt(workLog.getCreatedAt());
        dto.setCreatedBy(workLog.getCreatedBy());
        dto.setUpdatedAt(workLog.getUpdatedAt());
        dto.setUpdatedBy(workLog.getUpdatedBy());
        dto.setArchivedAt(workLog.getArchivedAt());
        dto.setArchivedBy(workLog.getArchivedBy());
        dto.setArchived(workLog.isArchived());
        dto.setRequestedDate(workLog.getRequestedDate());
        dto.setCompletedDate(workLog.getCompletedDate());
        dto.setUnitPrice(workLog.getUnitPrice());
        dto.setNotes(workLog.getNotes());
        dto.setToothNumbers(workLog.getToothNumbers());
        dto.setTechnicianProcedureId(workLog.getTechnicianProcedureId());
        dto.setTechnicianId(workLog.getTechnicianId());
        dto.setQuantity(workLog.getQuantity());
        dto.setToothNumbers(workLog.getToothNumbers());
        dto.setPatientId(workLog.getPatientId());
        dto.setIsPaid(workLog.getIsPaid());
        dto.setPaymentDate(workLog.getPaymentDate());

        return dto;
    }
}
