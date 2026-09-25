package com.dentistarchive.utils;

import com.dentistarchive.service.RentMonthlyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class RentMonthlyReportScheduler {

    private final RentMonthlyReportService rentMonthlyReportService;

    @Scheduled(cron = "0 0 0 1 * *")
    public void generateCurrentMonth() {
        rentMonthlyReportService.processCurrentMonth(YearMonth.now());
    }

}