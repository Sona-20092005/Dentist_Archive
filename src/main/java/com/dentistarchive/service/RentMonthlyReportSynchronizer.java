package com.dentistarchive.service;

import com.dentistarchive.entity.rent.ClinicRentAgreement;
import com.dentistarchive.entity.rent.RentMonthlyReport;
import com.dentistarchive.entity.rent.RentOvertime;
import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.enums.ModificationType;
import com.dentistarchive.enums.RentCalculationType;
import com.dentistarchive.enums.WorkSessionType;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.YearMonth;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentMonthlyReportSynchronizer {
    // TODO: 9/25/2026 exceptions

    private final WorkScheduleModificationRepository modificationRepository;
    private final WorkScheduleRepository scheduleRepository;
    private final ClinicRentAgreementRepository rentAgreementRepository;
    private final RentOvertimeRepository rentOvertimeRepository;
    private final RentMonthlyReportRepository rentMonthlyReportRepository;
    private final RentMonthlyReportGenerator rentMonthlyReportGenerator;

    @Transactional
    public void applyModificationCreate(WorkScheduleModification modification) {
        WorkSchedule schedule = scheduleRepository.getByIdAndNotArchived(modification.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, modification.getScheduleId()));
        ClinicRentAgreement rentAgreement = rentAgreementRepository.findEffectiveAgreement(schedule.getClinicId(), YearMonth.from(modification.getDate()))
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, modification.getScheduleId()));
        
        BigDecimal hourlyRent;
        if(modification.getWorkSessionType() == WorkSessionType.REGULAR) {
            hourlyRent = rentAgreement.getHourlyRate();
        }
        else {
            RentOvertime rentOvertime = rentOvertimeRepository.findByWorkScheduleModificationId(modification.getId())
                    .orElseThrow(() -> new EntityNotFoundByIdException(RentOvertime.class, modification.getId()));
            hourlyRent = rentOvertime.getHourlyRent();
        }

        switch(modification.getModificationType()) {
            case ModificationType.ADD -> {
                applyModificationCreateAdd(rentAgreement, modification, hourlyRent);
            }
            case ModificationType.MODIFY ->  {
                applyModificationCreateModify(rentAgreement, modification, hourlyRent);
            }
            case ModificationType.CANCEL -> {
                applyModificationCreateCancel(rentAgreement, modification);
            }
        }
    }
    
    private void applyModificationCreateAdd(ClinicRentAgreement rentAgreement, WorkScheduleModification modification, BigDecimal hourlyRent) {
        RentMonthlyReport report = getOrCreateReport(rentAgreement, modification, false);
        validateReportCanBeModified(report);
        withModificationAdd(report, rentAgreement, modification, hourlyRent, false);
        rentMonthlyReportRepository.save(report);
    }

    private void applyModificationCreateCancel(ClinicRentAgreement rentAgreement, WorkScheduleModification modification) {
        RentMonthlyReport sourceReport = getOrCreateReport(rentAgreement, modification, true);
        validateReportCanBeModified(sourceReport);
        withModificationRemove(sourceReport, rentAgreement, modification, rentAgreement.getHourlyRate(), true);
        rentMonthlyReportRepository.save(sourceReport);
    }

    private void applyModificationCreateModify(ClinicRentAgreement rentAgreement, WorkScheduleModification modification, BigDecimal hourlyRent) {
        RentMonthlyReport report = getOrCreateReport(rentAgreement, modification, false);
        validateReportCanBeModified(report);
        RentMonthlyReport sourceReport;
        
        if (YearMonth.from(modification.getDate()).equals(YearMonth.from(modification.getSourceDate()))) {
            sourceReport = report;
        }
        else {
            sourceReport = getOrCreateReport(rentAgreement, modification, true);
            validateReportCanBeModified(sourceReport);
        }

        withModificationAdd(report, rentAgreement, modification, hourlyRent, false);
        withModificationRemove(sourceReport, rentAgreement, modification, rentAgreement.getHourlyRate(), true);

        rentMonthlyReportRepository.save(report);
        if (!YearMonth.from(modification.getDate()).equals(YearMonth.from(modification.getSourceDate()))) {
            rentMonthlyReportRepository.save(sourceReport);
        }
    }
    

    @Transactional
    public void applyModificationDelete(WorkScheduleModification modification) {
        WorkSchedule schedule = scheduleRepository.getByIdAndNotArchived(modification.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, modification.getScheduleId()));
        ClinicRentAgreement rentAgreement = rentAgreementRepository.findEffectiveAgreement(schedule.getClinicId(), YearMonth.from(modification.getDate()))
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, modification.getScheduleId()));

        BigDecimal hourlyRent;
        if(modification.getWorkSessionType() == WorkSessionType.REGULAR) {
            hourlyRent = rentAgreement.getHourlyRate();
        }
        else {
            RentOvertime rentOvertime = rentOvertimeRepository.findByWorkScheduleModificationId(modification.getId())
                    .orElseThrow(() -> new EntityNotFoundByIdException(RentOvertime.class, modification.getId()));
            hourlyRent = rentOvertime.getHourlyRent();
        }

        switch(modification.getModificationType()) {
            case ModificationType.ADD -> {
                applyModificationDeleteAdd(rentAgreement, modification, hourlyRent);
            }
            case ModificationType.MODIFY ->  {
                applyModificationDeleteModify(rentAgreement, modification, hourlyRent);
            }
            case ModificationType.CANCEL -> {
                applyModificationDeleteCancel(rentAgreement, modification);
            }
        }
    }

    private void applyModificationDeleteAdd(ClinicRentAgreement rentAgreement, WorkScheduleModification modification, BigDecimal hourlyRent) {
        RentMonthlyReport report = getOrCreateReport(rentAgreement, modification, false);
        validateReportCanBeModified(report);
        withModificationRemove(report, rentAgreement, modification, hourlyRent, false);
        rentMonthlyReportRepository.save(report);
    }
    
    private void applyModificationDeleteCancel(ClinicRentAgreement rentAgreement, WorkScheduleModification modification) {
        RentMonthlyReport sourceReport = getOrCreateReport(rentAgreement, modification, true);
        validateReportCanBeModified(sourceReport);
        withModificationAdd(sourceReport, rentAgreement, modification, rentAgreement.getHourlyRate(), true);
        rentMonthlyReportRepository.save(sourceReport);
    }

    private void applyModificationDeleteModify(ClinicRentAgreement rentAgreement, WorkScheduleModification modification, BigDecimal hourlyRent) {
        RentMonthlyReport report = getOrCreateReport(rentAgreement, modification, false);
        validateReportCanBeModified(report);
        RentMonthlyReport sourceReport;

        if (YearMonth.from(modification.getDate()).equals(YearMonth.from(modification.getSourceDate()))) {
            sourceReport = report;
        }
        else {
            sourceReport = getOrCreateReport(rentAgreement, modification, true);
            validateReportCanBeModified(sourceReport);
        }

        withModificationRemove(report, rentAgreement, modification, hourlyRent, false);
        withModificationAdd(sourceReport, rentAgreement, modification, rentAgreement.getHourlyRate(), true);

        rentMonthlyReportRepository.save(report);
        if (!YearMonth.from(modification.getDate()).equals(YearMonth.from(modification.getSourceDate()))) {
            rentMonthlyReportRepository.save(sourceReport);
        }
    }
    
    @Transactional
    public void applyModificationUpdate(WorkScheduleModification oldModification, WorkScheduleModification newModification) {
        YearMonth oldMonth = YearMonth.from(oldModification.getDate());
        YearMonth newMonth = YearMonth.from(newModification.getDate());

        if (oldMonth.equals(newMonth)) {
            applyModificationUpdateWithoutDateChange(oldModification, newModification);
        }
        else {
            applyModificationUpdateWithDateChange(oldModification, newModification);
        }
    }

    @Transactional
    public void applyModificationUpdateWithoutDateChange(WorkScheduleModification oldModification, WorkScheduleModification newModification) {
        WorkSchedule schedule = scheduleRepository.getByIdAndNotArchived(oldModification.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, oldModification.getScheduleId()));
        ClinicRentAgreement rentAgreement = rentAgreementRepository.findEffectiveAgreement(schedule.getClinicId(), YearMonth.from(oldModification.getDate()))
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, oldModification.getScheduleId()));

        BigDecimal hourlyRent;
        if(oldModification.getWorkSessionType() == WorkSessionType.REGULAR) {
            hourlyRent = rentAgreement.getHourlyRate();
        }
        else {
            RentOvertime rentOvertime = rentOvertimeRepository.findByWorkScheduleModificationId(oldModification.getId())
                    .orElseThrow(() -> new EntityNotFoundByIdException(RentOvertime.class, oldModification.getId()));
            hourlyRent = rentOvertime.getHourlyRent();
        }

        // TODO: 10/2/2026 should we handle this here? 
        switch(oldModification.getModificationType()) {
            case ModificationType.ADD, ModificationType.MODIFY -> {
                RentMonthlyReport report = getOrCreateReport(rentAgreement, oldModification, false);
                validateReportCanBeModified(report);
                withModificationRemove(report, rentAgreement, oldModification, hourlyRent, false);
                withModificationAdd(report, rentAgreement, newModification, hourlyRent, false);

                rentMonthlyReportRepository.save(report);
            }
            default -> throw new IllegalStateException("Cancel Modification was updates by mistake");
        }
    }

    @Transactional
    public void applyModificationUpdateWithDateChange(WorkScheduleModification oldModification, WorkScheduleModification newModification) {
        WorkSchedule oldSchedule = scheduleRepository.getByIdAndNotArchived(oldModification.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, oldModification.getScheduleId()));
        WorkSchedule newSchedule = scheduleRepository.getByIdAndNotArchived(newModification.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, newModification.getScheduleId()));
        ClinicRentAgreement oldRentAgreement = rentAgreementRepository.findEffectiveAgreement(oldSchedule.getClinicId(), YearMonth.from(oldModification.getDate()))    
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, oldModification.getScheduleId()));
        ClinicRentAgreement newRentAgreement = rentAgreementRepository.findEffectiveAgreement(newSchedule.getClinicId(), YearMonth.from(newModification.getDate()))
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, newModification.getScheduleId()));
        
        BigDecimal oldHourlyRent;
        if(oldModification.getWorkSessionType() == WorkSessionType.REGULAR) {
            oldHourlyRent = oldRentAgreement.getHourlyRate();
        }
        else {
            RentOvertime rentOvertime = rentOvertimeRepository.findByWorkScheduleModificationId(oldModification.getId())
                    .orElseThrow(() -> new EntityNotFoundByIdException(RentOvertime.class, oldModification.getId()));
            oldHourlyRent = rentOvertime.getHourlyRent();
        }
        BigDecimal newHourlyRent;
        if(newModification.getWorkSessionType() == WorkSessionType.REGULAR) {
            newHourlyRent = newRentAgreement.getHourlyRate();
        }
        else {
            RentOvertime rentOvertime = rentOvertimeRepository.findByWorkScheduleModificationId(newModification.getId())
                    .orElseThrow(() -> new EntityNotFoundByIdException(RentOvertime.class, newModification.getId()));
            newHourlyRent = rentOvertime.getHourlyRent();
        }

        switch(oldModification.getModificationType()) {
            case ModificationType.ADD, ModificationType.MODIFY  -> {
                RentMonthlyReport oldReport = getOrCreateReport(oldRentAgreement, oldModification, false);
                RentMonthlyReport newReport = getOrCreateReport(oldRentAgreement, oldModification, false);
                validateReportCanBeModified(oldReport);
                validateReportCanBeModified(newReport);

                withModificationRemove(oldReport, oldRentAgreement, oldModification, oldHourlyRent, false);
                withModificationAdd(newReport, newRentAgreement, newModification, newHourlyRent, false);

                rentMonthlyReportRepository.save(oldReport);
                rentMonthlyReportRepository.save(newReport);
            }
            default -> throw new IllegalStateException("Cancel Modification was updated by mistake");
        }
    }

    private void withModificationAdd(RentMonthlyReport report, ClinicRentAgreement rentAgreement, WorkScheduleModification modification, BigDecimal hourlyRent, boolean useSource) {
        addMinutes(report, modification, useSource);

        if(rentAgreement.getRentCalculationType() == RentCalculationType.HOURLY ||
                (rentAgreement.getRentCalculationType() == RentCalculationType.HYBRID
                        && rentAgreement.getHourlyRate() != null)) {
            addAmounts(report, hourlyRent, modification, useSource);
        }
    }

    private void withModificationRemove(RentMonthlyReport report, ClinicRentAgreement rentAgreement, WorkScheduleModification modification, BigDecimal hourlyRent, boolean useSource) {
        removeMinutes(report, modification, useSource);

        if(rentAgreement.getRentCalculationType() == RentCalculationType.HOURLY ||
                (rentAgreement.getRentCalculationType() == RentCalculationType.HYBRID
                        && rentAgreement.getHourlyRate() != null)) {
            removeAmounts(report, hourlyRent, modification, useSource);
        }
    }
    
    @Transactional
    public void applyRentOvertimeUpdate(RentOvertime oldRentOvertime, RentOvertime newRentOvertime) {
        WorkScheduleModification modification = modificationRepository.getByIdAndNotArchived(oldRentOvertime.getWorkScheduleModificationId())
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkScheduleModification.class, oldRentOvertime.getWorkScheduleModificationId()));
        WorkSchedule schedule = scheduleRepository.getByIdAndNotArchived(modification.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, modification.getScheduleId()));
        ClinicRentAgreement rentAgreement = rentAgreementRepository.findEffectiveAgreement(schedule.getClinicId(), YearMonth.from(modification.getDate()))
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, modification.getScheduleId()));

        RentMonthlyReport report = getExistingReport(rentAgreement, modification);

        validateReportCanBeModified(report);
        removeAmounts(report, oldRentOvertime.getHourlyRent(), modification, false);
        addAmounts(report, newRentOvertime.getHourlyRent(), modification, false);

        validateReportValues(report);

        rentMonthlyReportRepository.save(report);
    }

    // TODO: 10/2/2026 apply updates to clinicRentAgreement syncronizer method


    
    private RentMonthlyReport getOrCreateReport(ClinicRentAgreement rentAgreement, WorkScheduleModification modification, boolean useSource) {
        YearMonth month;

        if(useSource) {
           month = YearMonth.from(modification.getSourceDate());
        }
        else {
            month = YearMonth.from(modification.getDate());
        }

        return rentMonthlyReportGenerator.getOrCreateReport(rentAgreement, month);
    }

    private RentMonthlyReport getExistingReport(ClinicRentAgreement rentAgreement, WorkScheduleModification modification) {
        YearMonth month = YearMonth.from(modification.getDate());

        return rentMonthlyReportRepository
                .findByClinicRentAgreementIdAndYearMonth(rentAgreement.getId(), month)
                .orElseThrow(() ->
                new EntityNotFoundByIdException(RentMonthlyReport.class, rentAgreement.getId())
        );
    }

    private int calculateMinutes(WorkScheduleModification modification, boolean useSource) {
        if(useSource) {
            return (int) Duration.between(modification.getSourceStartTime(), modification.getSourceEndTime()).toMinutes();
        }
        else {
            return (int) Duration.between(modification.getStartTime(), modification.getEndTime()).toMinutes();
        }
    }

    private void addMinutes(RentMonthlyReport report, WorkScheduleModification modification, boolean useSource) {
        int minutes = calculateMinutes(modification, useSource);

        if ((modification.getWorkSessionType() == WorkSessionType.REGULAR) || useSource) {
            report.setRegularMinutesUsed(report.getRegularMinutesUsed() + minutes);
        }
        else {
            report.setOvertimeMinutesUsed(report.getOvertimeMinutesUsed() + minutes);
        }
    }

    private void addAmounts(RentMonthlyReport report, BigDecimal hourlyRent, WorkScheduleModification modification, boolean useSource) {
        if ((modification.getWorkSessionType() == WorkSessionType.REGULAR) || useSource) {
            report.setRegularAmount(report.getRegularAmount().add(calculateAmount(hourlyRent, modification, useSource)));
        }
        else {
            report.setOvertimeAmount(report.getOvertimeAmount().add(calculateAmount(hourlyRent, modification, useSource)));
        }
    }

    private void removeMinutes(RentMonthlyReport report, WorkScheduleModification modification, boolean useSource) {
        int minutes = calculateMinutes(modification, useSource);

        if ((modification.getWorkSessionType() == WorkSessionType.REGULAR) || useSource) {
            report.setRegularMinutesUsed(report.getRegularMinutesUsed() - minutes);
        }
        else {
            report.setOvertimeMinutesUsed(report.getOvertimeMinutesUsed() - minutes);
        }
    }

    private void removeAmounts(RentMonthlyReport report, BigDecimal hourlyRent, WorkScheduleModification modification, boolean useSource) {
        if ((modification.getWorkSessionType() == WorkSessionType.REGULAR) || useSource) {
            report.setRegularAmount(report.getRegularAmount().subtract(calculateAmount(hourlyRent, modification, useSource)));
        }
        else {
            report.setOvertimeAmount(report.getOvertimeAmount().subtract(calculateAmount(hourlyRent, modification, useSource)));
        }
    }

    private BigDecimal calculateAmount(BigDecimal hourlyRent, WorkScheduleModification modification, boolean useSource) {

        return BigDecimal.valueOf(calculateMinutes(modification, useSource))
                .multiply(hourlyRent)
                .divide(
                        BigDecimal.valueOf(60),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private void validateReportCanBeModified(RentMonthlyReport report) {
        if (report.getLocked()) {
            throw new IllegalStateException(
                    "Report " + report.getId() + " is locked"
            );
        }
    }

    // TODO: 8/11/2026 decide if this is needed or a better way to handle this
    private void validateReportValues(RentMonthlyReport report) {
        if (report.getRegularMinutesUsed() < 0
                || report.getOvertimeMinutesUsed() < 0
                || report.getRegularAmount().signum() < 0
                || report.getOvertimeAmount().signum() < 0) {

            throw new IllegalStateException("Report values became negative: " + report.getId());
        }
    }

//    @Transactional
//    public void applyCurrentMonthCompensationTypeChange(Nurse nurse, CompensationType newCompensationType, YearMonth effectiveFrom) {
//
//        List<NurseWorkLog> regularWorkLogs =
//                nurseWorkLogRepository.findByNurseIdAndDateBetweenAndWorkType(
//                        nurse.getId(), effectiveFrom.atDay(1),
//                        effectiveFrom.atEndOfMonth(), NurseWorkType.REGULAR
//                        );
//
//        NursePayroll payroll = getPayroll(nurse.getId(), effectiveFrom);
//
//        validatePayrollCanBeModified(payroll);
//
//        if (newCompensationType == CompensationType.SALARY) {
//
//            setRegularWorkLogHourlyRates(regularWorkLogs, null);
//
//            BigDecimal salary = nurse.getBaseSalary();
//
//            payroll.setCalculatedRegularAmount(salary);
//            payroll.setRegularAmount(salary);
//        }
//        else if (newCompensationType == CompensationType.HOURLY) {
//
//            BigDecimal regularHourlyRate = nurse.getHourlyRate();
//
//            setRegularWorkLogHourlyRates(regularWorkLogs, regularHourlyRate);
//
//            BigDecimal calculatedRegularAmount = calculateHourlyRegularAmount(
//                            payroll.getRegularMinutesWorked(),
//                            regularHourlyRate
//                    );
//
//            payroll.setCalculatedRegularAmount(calculatedRegularAmount);
//            payroll.setRegularAmount(calculatedRegularAmount);
//        }
//
//        validatePayrollValues(payroll);
//
//        nurseWorkLogRepository.saveAll(regularWorkLogs);
//
//        payroll.setCompensationType(newCompensationType);
//        nursePayrollRepository.save(payroll);
//
//    }

    private RentMonthlyReport getReport(UUID clinicRentAgreementId, YearMonth yearMonth) {
        return rentMonthlyReportRepository.findByClinicRentAgreementIdAndYearMonth(clinicRentAgreementId, yearMonth)
                .orElseThrow(() -> new EntityNotFoundByIdException(RentMonthlyReport.class, clinicRentAgreementId));
    }

}