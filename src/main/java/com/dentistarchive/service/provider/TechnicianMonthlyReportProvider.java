package com.dentistarchive.service.provider;

import com.dentistarchive.dto.update.TechnicianMonthlyReportUpdateDto;
import com.dentistarchive.entity.technician.TechnicianMonthlyReport;
import org.springframework.stereotype.Component;


@Component
public class TechnicianMonthlyReportProvider {

    public TechnicianMonthlyReport update(TechnicianMonthlyReport report, TechnicianMonthlyReportUpdateDto updateDto) {

        report.setStatus(updateDto.getStatus());
        report.setTotalAmount(updateDto.getTotalAmount());
        report.setPaidAmount(updateDto.getPaidAmount());
        report.setBonus(updateDto.getBonus());
        report.setDeduction(updateDto.getDeduction());
        report.setNotes(updateDto.getNotes());

        return report;
    }
}
