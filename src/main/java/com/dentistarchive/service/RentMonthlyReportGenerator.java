package com.dentistarchive.service;

import com.dentistarchive.dto.WorkSessionDto;
import com.dentistarchive.entity.rent.ClinicRentAgreement;
import com.dentistarchive.entity.rent.RentMonthlyReport;
import com.dentistarchive.enums.RentCalculationType;
import com.dentistarchive.enums.RentMonthlyReportStatus;
import com.dentistarchive.enums.WorkSessionType;
import com.dentistarchive.repository.ClinicRentAgreementRepository;
import com.dentistarchive.repository.RentMonthlyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentMonthlyReportGenerator {

    private final ClinicRentAgreementRepository clinicRentAgreementRepository;
    private final RentMonthlyReportRepository reportRepository;
    private final WorkCalendarService workCalendarService;

    private RentMonthlyReport createReport(ClinicRentAgreement rentAgreement, YearMonth yearMonth) {

        RentMonthlyReport report = RentMonthlyReport.builder()
                .clinicRentAgreementId(rentAgreement.getId())
                .yearMonth(yearMonth)
                .status(RentMonthlyReportStatus.DRAFT)
                .locked(false)
                .build();

        initializeRegularRent(report, rentAgreement, yearMonth);
// TODO: existing overtime into the numbers

        return report;

    }

    // To be run at the start of the month
    @Transactional
    public void generateReports(YearMonth yearMonth) {

        LocalDate monthStart = yearMonth.atDay(1);

        List<ClinicRentAgreement> rentAgreements = clinicRentAgreementRepository.findAllForReportGeneration(monthStart);

        for (ClinicRentAgreement rentAgreement : rentAgreements) {
            if (reportRepository.existsByClinicRentAgreementIdAndYearMonth(rentAgreement.getId(), yearMonth)) {
                continue;
            }

            reportRepository.save(createReport(rentAgreement, yearMonth));
        }
    }

    // to be run when a new clinic rent agreement is created or when a clinicRentAgreement's monthly report effective from date is updated
    @Transactional
    public void generateInitialReports(ClinicRentAgreement clinicRentAgreement) {

        YearMonth start = YearMonth.from(clinicRentAgreement.getEffectiveFrom());
        YearMonth current = YearMonth.now();

        while (!start.isAfter(current)) {
            if (!reportRepository.existsByClinicRentAgreementIdAndYearMonth(clinicRentAgreement.getId(), start)) {
                reportRepository.save(createReport(clinicRentAgreement, start));
            }

            start = start.plusMonths(1);
        }
    }

    public RentMonthlyReport getOrCreateReport(ClinicRentAgreement clinicRentAgreement, YearMonth month) {
        return reportRepository
                .findByClinicRentAgreementIdAndYearMonth(clinicRentAgreement.getId(), month)
                .orElseGet(() -> reportRepository.save(createReport(clinicRentAgreement, month)));
    }

    private void initializeRegularRent(RentMonthlyReport report, ClinicRentAgreement rentAgreement, YearMonth yearMonth) {
        RentCalculationType calculationType = rentAgreement.getRentCalculationType();

        int regularMinutes = calculateRegularUsedMinutes(report, rentAgreement, yearMonth);
        report.setRegularMinutesUsed(regularMinutes);

        if (calculationType == RentCalculationType.FIXED_MONTHLY) {
            report.setRegularAmount(rentAgreement.getFixedMonthlyRent());
        }
        else if (calculationType == RentCalculationType.HOURLY) {
            report.setRegularAmount(calculateHourlyAmount(report, rentAgreement, regularMinutes));
        }
        else if (calculationType == RentCalculationType.HYBRID) {
            if (rentAgreement.getFixedMonthlyRent() != null) {
                report.setRegularAmount(report.getRegularAmount().add(rentAgreement.getFixedMonthlyRent()));
            }
            if (rentAgreement.getHourlyRate() != null) {
                report.setRegularAmount(report.getRegularAmount().add(calculateHourlyAmount(report, rentAgreement, regularMinutes)));
            }
        }
    }

    private int calculateRegularUsedMinutes(RentMonthlyReport report, ClinicRentAgreement rentAgreement, YearMonth yearMonth) {
        int regularMinutes = 0;
        List<WorkSessionDto> sessions = workCalendarService.getClinicSessions(
                rentAgreement.getClinicId(),
                yearMonth.atDay(1),
                yearMonth.atEndOfMonth());

        for (WorkSessionDto session : sessions) {
            if (session.getWorkSessionType() != WorkSessionType.REGULAR) {
                continue;
            }

            regularMinutes += calculateMinutes(session);
        }
        return regularMinutes;
    }

    private BigDecimal calculateHourlyAmount(RentMonthlyReport report, ClinicRentAgreement rentAgreement, int minutes) {
        return BigDecimal.valueOf(minutes)
                .multiply(rentAgreement.getHourlyRate())
                .divide(BigDecimal.valueOf(60),
                        2,
                        RoundingMode.HALF_UP);
    }

    private int calculateMinutes(WorkSessionDto session) {
        return (int) Duration.between(session.getStartTime(), session.getEndTime()).toMinutes();
    }
}