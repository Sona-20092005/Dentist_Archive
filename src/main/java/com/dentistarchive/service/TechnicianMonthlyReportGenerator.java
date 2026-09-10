package com.dentistarchive.service;

import com.dentistarchive.entity.technician.Technician;
import com.dentistarchive.entity.technician.TechnicianMonthlyReport;
import com.dentistarchive.enums.TechnicianMonthlyReportStatus;
import com.dentistarchive.repository.TechnicianMonthlyReportRepository;
import com.dentistarchive.repository.TechnicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianMonthlyReportGenerator {

    private final TechnicianRepository technicianRepository;
    private final TechnicianMonthlyReportRepository reportRepository;

    private TechnicianMonthlyReport createReport(Technician technician, YearMonth yearMonth) {

        return TechnicianMonthlyReport.builder()
                .technicianId(technician.getId())
                .yearMonth(yearMonth)
                .status(TechnicianMonthlyReportStatus.DRAFT)
                .locked(false)
                .build();
    }

    // To be run at the start of the month
    @Transactional
    public void generateReports(YearMonth yearMonth) {

        LocalDate monthStart = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();

        List<Technician> technicians = technicianRepository.findAllForReportGeneration(monthStart, monthEnd);

        for (Technician technician : technicians) {
            if (reportRepository.existsByTechnicianIdAndYearMonth(technician.getId(), yearMonth)) {
                continue;
            }

            reportRepository.save(createReport(technician, yearMonth));
        }
    }

    // to be run when a new technician is created or when a technician's monthly report start date is updated
    @Transactional
    public void generateInitialReports(Technician technician) {

        YearMonth start = YearMonth.from(technician.getMonthlyReportStartDate());
        YearMonth current = YearMonth.now();

        while (!start.isAfter(current)) {
            if (!reportRepository.existsByTechnicianIdAndYearMonth(technician.getId(), start)) {
                reportRepository.save(createReport(technician, start));
            }

            start = start.plusMonths(1);
        }
    }

    public TechnicianMonthlyReport getOrCreateReport(Technician technician, YearMonth month) {
        return reportRepository
                .findByTechnicianIdAndYearMonth(technician.getId(), month)
                .orElseGet(() -> reportRepository.save(createReport(technician, month)));
    }
}