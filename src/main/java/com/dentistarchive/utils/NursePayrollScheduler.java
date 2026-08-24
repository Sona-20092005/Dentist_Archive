package com.dentistarchive.utils;

import com.dentistarchive.service.NursePayrollGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class NursePayrollScheduler {

    private final NursePayrollGenerator generator;

    @Scheduled(cron = "0 0 0 1 * *")
    public void generateCurrentMonth() {
        generator.generatePayrolls(YearMonth.now());
    }
}