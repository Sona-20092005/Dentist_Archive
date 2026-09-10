package com.dentistarchive.utils;

import com.dentistarchive.service.TechnicianMonthlyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class TechnicianMonthlyReportScheduler {

    private final TechnicianMonthlyReportService technicianMonthlyReportService;

    @Scheduled(cron = "0 0 0 1 * *")
    public void generateCurrentMonth() {
        technicianMonthlyReportService.processCurrentMonth(YearMonth.now());
    }

}