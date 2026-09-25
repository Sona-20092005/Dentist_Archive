package com.dentistarchive.service.provider;

import com.dentistarchive.dto.update.RentMonthlyReportUpdateDto;
import com.dentistarchive.entity.rent.RentMonthlyReport;
import org.springframework.stereotype.Component;


@Component
public class RentMonthlyReportProvider {

    public RentMonthlyReport update(RentMonthlyReport report, RentMonthlyReportUpdateDto updateDto) {

        report.setManualAdjustment(updateDto.getManualAdjustment());
        report.setStatus(updateDto.getStatus());
        report.setNotes(updateDto.getNotes());

        return report;
    }
}
