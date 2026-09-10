package com.dentistarchive.service;

import com.dentistarchive.entity.technician.Technician;
import com.dentistarchive.entity.technician.TechnicianMonthlyReport;
import com.dentistarchive.entity.technician.TechnicianWorkLog;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.TechnicianMonthlyReportRepository;
import com.dentistarchive.repository.TechnicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class TechnicianMonthlyReportSynchronizer {

    private final TechnicianRepository technicianRepository;
    private final TechnicianMonthlyReportRepository technicianMonthlyReportRepository;
    private final TechnicianMonthlyReportGenerator technicianMonthlyReportGenerator;

    @Transactional
    public void applyWorkLogCreate(TechnicianWorkLog log) {

        Technician technician = technicianRepository.getByIdAndNotArchived(log.getTechnicianId())
                .orElseThrow(() -> new EntityNotFoundByIdException(Technician.class, log.getTechnicianId()));

        TechnicianMonthlyReport report = getOrCreateReport(technician, log);

        validateReportCanBeModified(report);

        addNumberOfWorkLogs(report);
        addTotalAmounts(report, log);
        addPaidAmounts(report, log);

        technicianMonthlyReportRepository.save(report);
    }

    @Transactional
    public void applyWorkLogDelete(TechnicianWorkLog log) {

        Technician technician = technicianRepository.getByIdAndNotArchived(log.getTechnicianId())
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Technician.class, log.getTechnicianId()));

        TechnicianMonthlyReport report = getExistingReport(technician, log);

        validateReportCanBeModified(report);

        removeNumberOfWorkLogs(report);
        removeTotalAmounts(report, log);
        removePaidAmounts(report, log);

        validateReportValues(report);

        technicianMonthlyReportRepository.save(report);
    }

    @Transactional
    public void applyWorkLogUpdate(TechnicianWorkLog oldLog, TechnicianWorkLog newLog) {

        YearMonth oldMonth = YearMonth.from(oldLog.getCompletedDate());
        YearMonth newMonth = YearMonth.from(newLog.getCompletedDate());

        if (oldMonth.equals(newMonth)) {
            applyWorkLogUpdateWithoutDateChange(oldLog, newLog);
        }
        else {
            applyWorkLogUpdateWithDateChange(oldLog, newLog);
        }

    }

    private void applyWorkLogUpdateWithoutDateChange(TechnicianWorkLog oldLog, TechnicianWorkLog newLog) {
        Technician technician = technicianRepository.getByIdAndNotArchived(newLog.getTechnicianId()).orElseThrow(() ->
                new EntityNotFoundByIdException(Technician.class, newLog.getTechnicianId()));

        TechnicianMonthlyReport report = getExistingReport(technician, newLog);

        validateReportCanBeModified(report);

        removeTotalAmounts(report, oldLog);
        removePaidAmounts(report, oldLog);

        addTotalAmounts(report, newLog);
        addPaidAmounts(report, newLog);

        validateReportValues(report);

        technicianMonthlyReportRepository.save(report);
    }

    private void applyWorkLogUpdateWithDateChange(TechnicianWorkLog oldLog, TechnicianWorkLog newLog) {
        Technician technician = technicianRepository.getByIdAndNotArchived(newLog.getTechnicianId())
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Technician.class, newLog.getTechnicianId()));

        TechnicianMonthlyReport oldReport = getExistingReport(technician, oldLog);
        TechnicianMonthlyReport newReport = getExistingReport(technician, newLog);

        validateReportCanBeModified(oldReport);
        validateReportCanBeModified(newReport);

        removeTotalAmounts(oldReport, oldLog);
        removePaidAmounts(oldReport, oldLog);
        removeNumberOfWorkLogs(oldReport);

        addTotalAmounts(newReport, newLog);
        addPaidAmounts(newReport, newLog);
        addNumberOfWorkLogs(newReport);

        validateReportValues(oldReport);
        validateReportValues(newReport);

        technicianMonthlyReportRepository.save(oldReport);
        technicianMonthlyReportRepository.save(newReport);
    }


    private TechnicianMonthlyReport getOrCreateReport(Technician technician, TechnicianWorkLog log) {
        YearMonth month = YearMonth.from(log.getCompletedDate());

        return technicianMonthlyReportGenerator.getOrCreateReport(technician, month);
    }

    private TechnicianMonthlyReport getExistingReport(Technician technician, TechnicianWorkLog log) {
        YearMonth yearMonth = YearMonth.from(log.getCompletedDate());

        return technicianMonthlyReportRepository
                .findByTechnicianIdAndYearMonth(technician.getId(), yearMonth)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(TechnicianMonthlyReport.class, technician.getId())
                );
    }

    private void addNumberOfWorkLogs(TechnicianMonthlyReport report) {
        report.setNumberOfWorkLogs(report.getNumberOfWorkLogs() + 1);
    }

    private void addTotalAmounts(TechnicianMonthlyReport report, TechnicianWorkLog log) {
        report.setTotalAmount(report.getTotalAmount().add(calculateAmount(log)));
    }

    private void addPaidAmounts(TechnicianMonthlyReport report, TechnicianWorkLog log) {
        if (log.getIsPaid()) {
            report.setPaidAmount(report.getPaidAmount().add(calculateAmount(log)));
        }
    }

    private void removeNumberOfWorkLogs(TechnicianMonthlyReport report) {
        report.setNumberOfWorkLogs(report.getNumberOfWorkLogs() - 1);
    }

    private void removeTotalAmounts(TechnicianMonthlyReport report, TechnicianWorkLog log) {
        report.setTotalAmount(report.getTotalAmount().subtract(calculateAmount(log)));
    }

    private void removePaidAmounts(TechnicianMonthlyReport report, TechnicianWorkLog log) {
        if (log.getIsPaid()) {
            report.setPaidAmount(report.getPaidAmount().subtract(calculateAmount(log)));
        }
    }

    private BigDecimal calculateAmount(TechnicianWorkLog log) {

        return BigDecimal.valueOf(log.getQuantity()).multiply(log.getUnitPrice());
    }

    private void validateReportCanBeModified(TechnicianMonthlyReport report) {
        if (report.getLocked()) {
            throw new IllegalStateException(
                    "Report " + report.getId() + " is locked"
            );
        }
    }

    // TODO: 8/11/2026 decide if this is needed or a better way to handle this
    private void validateReportValues(TechnicianMonthlyReport report) {
        if (report.getNumberOfWorkLogs() < 0
                || report.getTotalAmount().signum() < 0
                || report.getPaidAmount().signum() < 0) {

            throw new IllegalStateException("Report values became negative: " + report.getId());
        }
    }



}