package com.dentistarchive.service;

import com.dentistarchive.entity.nurse.Nurse;
import com.dentistarchive.entity.nurse.NursePayroll;
import com.dentistarchive.entity.nurse.NurseWorkLog;
import com.dentistarchive.enums.CompensationType;
import com.dentistarchive.enums.NurseWorkType;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.NursePayrollRepository;
import com.dentistarchive.repository.NurseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class NursePayrollSynchronizer {

    private final NurseRepository nurseRepository;
    private final NursePayrollRepository nursePayrollRepository;
    private final NursePayrollGenerator nursePayrollGenerator;


    @Transactional
    public void applyWorkLogCreate(NurseWorkLog log) {

        Nurse nurse = nurseRepository.getByIdAndNotArchived(log.getNurseId())
                .orElseThrow(() -> new EntityNotFoundByIdException(Nurse.class, log.getNurseId()));

        NursePayroll payroll = getOrCreatePayroll(nurse, log);

        validatePayrollCanBeModified(payroll);

        addMinutes(payroll, log);
        addAmounts(payroll, log);
        updateDisplayedAmounts(payroll);

        nursePayrollRepository.save(payroll);
    }

    @Transactional
    public void applyWorkLogDelete(NurseWorkLog log) {

        Nurse nurse = nurseRepository.getByIdAndNotArchived(log.getNurseId())
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Nurse.class, log.getNurseId()));

        NursePayroll payroll = getExistingPayroll(nurse, log);

        validatePayrollCanBeModified(payroll);

        removeMinutes(payroll, log);
        removeAmounts(payroll, log);

        updateDisplayedAmounts(payroll);

        validatePayrollValues(payroll);

        nursePayrollRepository.save(payroll);
    }

    @Transactional
    public void applyWorkLogUpdate(NurseWorkLog oldLog, NurseWorkLog newLog) {

        YearMonth oldMonth = YearMonth.from(oldLog.getDate());
        YearMonth newMonth = YearMonth.from(newLog.getDate());

        if (oldMonth.equals(newMonth)) {
            applyWorkLogUpdateWithoutDateChange(oldLog, newLog);
        }
        else {
            applyWorkLogUpdateWithDateChange(oldLog, newLog);
        }

    }

    private void applyWorkLogUpdateWithoutDateChange(NurseWorkLog oldLog, NurseWorkLog newLog) {
        Nurse nurse = nurseRepository.getByIdAndNotArchived(newLog.getNurseId()).orElseThrow(() ->
                new EntityNotFoundByIdException(Nurse.class, newLog.getNurseId()));

        NursePayroll payroll = getExistingPayroll(nurse, newLog);

        validatePayrollCanBeModified(payroll);

        removeMinutes(payroll, oldLog);
        removeAmounts(payroll, oldLog);

        addMinutes(payroll, newLog);
        addAmounts(payroll, newLog);

        updateDisplayedAmounts(payroll);

        validatePayrollValues(payroll);

        nursePayrollRepository.save(payroll);
    }

    private void applyWorkLogUpdateWithDateChange(NurseWorkLog oldLog, NurseWorkLog newLog) {
        Nurse nurse = nurseRepository.getByIdAndNotArchived(newLog.getNurseId())
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Nurse.class, newLog.getNurseId()));

        NursePayroll oldPayroll = getExistingPayroll(nurse, oldLog);
        NursePayroll newPayroll = getExistingPayroll(nurse, newLog);

        validatePayrollCanBeModified(oldPayroll);
        validatePayrollCanBeModified(newPayroll);

        removeMinutes(oldPayroll, oldLog);
        removeAmounts(oldPayroll, oldLog);
        updateDisplayedAmounts(oldPayroll);

        addMinutes(newPayroll, newLog);
        addAmounts(newPayroll, newLog);
        updateDisplayedAmounts(newPayroll);

        validatePayrollValues(oldPayroll);
        validatePayrollValues(newPayroll);

        nursePayrollRepository.save(oldPayroll);
        nursePayrollRepository.save(newPayroll);
    }


    private NursePayroll getOrCreatePayroll(Nurse nurse, NurseWorkLog log) {
        YearMonth month = YearMonth.from(log.getDate());

        return nursePayrollGenerator.getOrCreatePayroll(nurse, month);
    }

    private NursePayroll getExistingPayroll(Nurse nurse, NurseWorkLog log) {
        YearMonth month = YearMonth.from(log.getDate());

        return nursePayrollRepository
                .findByNurseIdAndYearAndMonth(nurse.getId(), month.getYear(), month.getMonth())
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(NursePayroll.class, nurse.getId())
                );
    }

    private int calculateMinutes(NurseWorkLog log) {
        return (int) Duration.between(log.getStartTime(), log.getEndTime()).toMinutes();
    }

    private void addMinutes(NursePayroll payroll, NurseWorkLog log) {
        int minutes = calculateMinutes(log);

        NurseWorkType workType = log.getWorkType();

        if (workType == NurseWorkType.REGULAR) {
            payroll.setRegularMinutesWorked(payroll.getRegularMinutesWorked() + minutes);
        }
        else {
            payroll.setOvertimeMinutesWorked(payroll.getOvertimeMinutesWorked() + minutes);
        }
    }

    private void addAmounts(NursePayroll payroll, NurseWorkLog log) {
        if (log.getWorkType() == NurseWorkType.OVERTIME) {
            payroll.setCalculatedOvertimeAmount(payroll.getCalculatedOvertimeAmount().add(calculateAmount(log)));
        }
        else if ((log.getWorkType() == NurseWorkType.REGULAR) && (payroll.getCompensationType() == CompensationType.HOURLY)) {
            payroll.setCalculatedRegularAmount(payroll.getCalculatedRegularAmount().add(calculateAmount(log)));
        }
    }

    private void removeMinutes(NursePayroll payroll, NurseWorkLog log) {
        int minutes = calculateMinutes(log);

        if (log.getWorkType() == NurseWorkType.REGULAR) {
            payroll.setRegularMinutesWorked(payroll.getRegularMinutesWorked() - minutes);
        }
        else {
            payroll.setOvertimeMinutesWorked(payroll.getOvertimeMinutesWorked() - minutes);
        }
    }

    private void removeAmounts(NursePayroll payroll, NurseWorkLog log) {
        if (log.getWorkType() == NurseWorkType.OVERTIME) {
            payroll.setCalculatedOvertimeAmount(payroll.getCalculatedOvertimeAmount().subtract(calculateAmount(log)));
        }
        else if (log.getWorkType() == NurseWorkType.REGULAR && payroll.getCompensationType() == CompensationType.HOURLY) {
            payroll.setCalculatedRegularAmount(payroll.getCalculatedRegularAmount().subtract(calculateAmount(log)));
        }
    }

    private void updateDisplayedAmounts(NursePayroll payroll) {
        if (!payroll.isRegularAmountManuallyAdjusted()) {
            payroll.setRegularAmount(payroll.getCalculatedRegularAmount());
        }
        if (!payroll.isOvertimeAmountManuallyAdjusted()) {
            payroll.setOvertimeAmount(payroll.getCalculatedOvertimeAmount());
        }
    }

    private BigDecimal calculateAmount(NurseWorkLog log) {

        return BigDecimal.valueOf(calculateMinutes(log))
                .multiply(log.getHourlyRate())
                .divide(
                        BigDecimal.valueOf(60),
                        2,
                        RoundingMode.HALF_UP
                );
    }

    private void validatePayrollCanBeModified(NursePayroll payroll) {
        if (payroll.getLocked()) {
            throw new IllegalStateException(
                    "Payroll " + payroll.getId() + " is locked"
            );
        }
    }

    // TODO: 8/11/2026 decide if this is needed or a better way to handle this
    private void validatePayrollValues(NursePayroll payroll) {
        if (payroll.getRegularMinutesWorked() < 0
                || payroll.getOvertimeMinutesWorked() < 0
                || payroll.getCalculatedRegularAmount().signum() < 0
                || payroll.getCalculatedOvertimeAmount().signum() < 0) {

            throw new IllegalStateException("Payroll values became negative: " + payroll.getId());
        }
    }
}