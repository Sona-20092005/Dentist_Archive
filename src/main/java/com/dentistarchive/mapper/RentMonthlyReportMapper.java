package com.dentistarchive.mapper;

import com.dentistarchive.dto.RentMonthlyReportDto;
import com.dentistarchive.entity.rent.RentMonthlyReport;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RentMonthlyReportMapper implements EntityMapper<RentMonthlyReport, RentMonthlyReportDto> {

    public RentMonthlyReportDto toDto(RentMonthlyReport rentMonthlyReport) {

        RentMonthlyReportDto dto = new RentMonthlyReportDto();

        dto.setId(rentMonthlyReport.getId());
        dto.setCreatedAt(rentMonthlyReport.getCreatedAt());
        dto.setCreatedBy(rentMonthlyReport.getCreatedBy());
        dto.setUpdatedAt(rentMonthlyReport.getUpdatedAt());
        dto.setUpdatedBy(rentMonthlyReport.getUpdatedBy());

        dto.setClinicRentAgreementId(rentMonthlyReport.getClinicRentAgreementId());
        dto.setStatus(rentMonthlyReport.getStatus());
        dto.setYearMonth(rentMonthlyReport.getYearMonth());
        dto.setRegularAmount(rentMonthlyReport.getRegularAmount());
        dto.setRegularMinutesUsed(rentMonthlyReport.getRegularMinutesUsed());
        dto.setOvertimeAmount(rentMonthlyReport.getOvertimeAmount());
        dto.setOvertimeMinutesUsed(rentMonthlyReport.getOvertimeMinutesUsed());
        dto.setManualAdjustment(rentMonthlyReport.getManualAdjustment());
        dto.setLocked(rentMonthlyReport.getLocked());
        dto.setNotes(rentMonthlyReport.getNotes());
        return dto;
    }

}
