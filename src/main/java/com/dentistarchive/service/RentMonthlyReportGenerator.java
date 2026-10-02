package com.dentistarchive.service;

import com.dentistarchive.dto.WorkSessionDto;
import com.dentistarchive.entity.rent.ClinicRentAgreement;
import com.dentistarchive.entity.rent.RentMonthlyReport;
import com.dentistarchive.entity.rent.RentOvertime;
import com.dentistarchive.enums.RentCalculationType;
import com.dentistarchive.enums.RentMonthlyReportStatus;
import com.dentistarchive.enums.WorkSessionType;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.ClinicRentAgreementRepository;
import com.dentistarchive.repository.RentMonthlyReportRepository;
import com.dentistarchive.repository.RentOvertimeRepository;
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
    private final RentOvertimeRepository rentOvertimeRepository;

    private RentMonthlyReport createReport(ClinicRentAgreement rentAgreement, YearMonth yearMonth) {

        RentMonthlyReport report = RentMonthlyReport.builder()
                .clinicRentAgreementId(rentAgreement.getId())
                .yearMonth(yearMonth)
                .status(RentMonthlyReportStatus.DRAFT)
                .locked(false)
                .build();

        initializeRegularRentAndMinutes(report, rentAgreement, yearMonth);
        initializeOvertimeRentAndMinutes(report, rentAgreement, yearMonth);

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

    // TODO: 9/29/2026 figure out if this is necessary
    // to be run when a new clinic rent agreement is created or when a clinicRentAgreement's monthly report effective from date is updated
//    @Transactional
//    public void generateInitialReports(ClinicRentAgreement clinicRentAgreement) {
//
//        YearMonth start = YearMonth.from(clinicRentAgreement.getEffectiveFrom());
//        YearMonth current = YearMonth.now();
//
//        while (!start.isAfter(current)) {
//            if (!reportRepository.existsByClinicRentAgreementIdAndYearMonth(clinicRentAgreement.getId(), start)) {
//                reportRepository.save(createReport(clinicRentAgreement, start));
//            }
//
//            start = start.plusMonths(1);
//        }
//    }

    public RentMonthlyReport getOrCreateReport(ClinicRentAgreement clinicRentAgreement, YearMonth month) {
        return reportRepository
                .findByClinicRentAgreementIdAndYearMonth(clinicRentAgreement.getId(), month)
                .orElseGet(() -> reportRepository.save(createReport(clinicRentAgreement, month)));
    }

    private void initializeRegularRentAndMinutes(RentMonthlyReport report, ClinicRentAgreement rentAgreement, YearMonth yearMonth) {
        RentCalculationType calculationType = rentAgreement.getRentCalculationType();

        int regularMinutes = calculateRegularUsedMinutes(report, rentAgreement, yearMonth);
        report.setRegularMinutesUsed(regularMinutes);

        if (calculationType == RentCalculationType.FIXED_MONTHLY) {
            report.setRegularAmount(rentAgreement.getFixedMonthlyRent());
        }
        else if (calculationType == RentCalculationType.HOURLY) {
            report.setRegularAmount(calculateHourlyAmount(rentAgreement, regularMinutes));
        }
        else if (calculationType == RentCalculationType.HYBRID) {
            if (rentAgreement.getFixedMonthlyRent() != null) {
                report.setRegularAmount(report.getRegularAmount().add(rentAgreement.getFixedMonthlyRent()));
            }
            if (rentAgreement.getHourlyRate() != null) {
                report.setRegularAmount(report.getRegularAmount().add(calculateHourlyAmount(rentAgreement, regularMinutes)));
            }
        }
    }

    private void initializeOvertimeRentAndMinutes(RentMonthlyReport report, ClinicRentAgreement rentAgreement, YearMonth yearMonth) {
        RentCalculationType calculationType = rentAgreement.getRentCalculationType();

        setOvertimeUsedMinutesAndAmounts(report, rentAgreement, yearMonth);;
    }

    private int setOvertimeUsedMinutesAndAmounts(RentMonthlyReport report, ClinicRentAgreement rentAgreement, YearMonth yearMonth) {
        int overtimeMinutes = 0;
        BigDecimal overtimeAmount = BigDecimal.ZERO;

        List<WorkSessionDto> sessions = workCalendarService.getClinicSessions(
                rentAgreement.getClinicId(),
                yearMonth.atDay(1),
                yearMonth.atEndOfMonth());

        for (WorkSessionDto session : sessions) {
            if (session.getWorkSessionType() != WorkSessionType.OVERTIME) {
                continue;
            }

            RentOvertime rentOvertime = rentOvertimeRepository.findByWorkScheduleModificationId(session.getModificationId())
                    .orElseThrow(() -> new EntityNotFoundByIdException(RentOvertime.class, session.getModificationId()));

            overtimeMinutes += calculateMinutes(session);
            overtimeAmount = overtimeAmount.add(calculateAmount(rentOvertime.getHourlyRent(), calculateMinutes(session)));
        }
        report.setOvertimeMinutesUsed(overtimeMinutes);
        report.setOvertimeAmount(overtimeAmount);
        return overtimeMinutes;
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



    private BigDecimal calculateHourlyAmount(ClinicRentAgreement rentAgreement, int minutes) {
        return calculateAmount(rentAgreement.getHourlyRate(), minutes);
    }

    private BigDecimal calculateAmount(BigDecimal hourlyRate, int minutes) {
        return BigDecimal.valueOf(minutes)
                .multiply(hourlyRate)
                .divide(BigDecimal.valueOf(60),
                        2,
                        RoundingMode.HALF_UP);
    }

    private int calculateMinutes(WorkSessionDto session) {
        return (int) Duration.between(session.getStartTime(), session.getEndTime()).toMinutes();
    }
}