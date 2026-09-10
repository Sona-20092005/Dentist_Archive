package com.dentistarchive.mapper;

import com.dentistarchive.dto.TechnicianMonthlyReportDto;
import com.dentistarchive.entity.technician.TechnicianMonthlyReport;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianMonthlyReportMapper implements EntityMapper<TechnicianMonthlyReport, TechnicianMonthlyReportDto> {

    public TechnicianMonthlyReportDto toDto(TechnicianMonthlyReport technicianMonthlyReport) {

        TechnicianMonthlyReportDto dto = new TechnicianMonthlyReportDto();

        dto.setId(technicianMonthlyReport.getId());
        dto.setCreatedAt(technicianMonthlyReport.getCreatedAt());
        dto.setCreatedBy(technicianMonthlyReport.getCreatedBy());
        dto.setUpdatedAt(technicianMonthlyReport.getUpdatedAt());
        dto.setUpdatedBy(technicianMonthlyReport.getUpdatedBy());

        dto.setTechnicianId(technicianMonthlyReport.getTechnicianId());
        dto.setStatus(technicianMonthlyReport.getStatus());
        dto.setYearMonth(technicianMonthlyReport.getYearMonth());
        dto.setTotalAmount(technicianMonthlyReport.getTotalAmount());
        dto.setPaidAmount(technicianMonthlyReport.getPaidAmount());
        dto.setBonus(technicianMonthlyReport.getBonus());
        dto.setDeduction(technicianMonthlyReport.getDeduction());
        dto.setNumberOfWorkLogs(technicianMonthlyReport.getNumberOfWorkLogs());
        dto.setLocked(technicianMonthlyReport.getLocked());
        dto.setNotes(technicianMonthlyReport.getNotes());
        return dto;
    }

}
