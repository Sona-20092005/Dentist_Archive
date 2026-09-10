package com.dentistarchive.service;

import com.dentistarchive.entity.nurse.Nurse;
import com.dentistarchive.entity.nurse.NursePayroll;
import com.dentistarchive.enums.CompensationType;
import com.dentistarchive.enums.NursePayrollStatus;
import com.dentistarchive.repository.NursePayrollRepository;
import com.dentistarchive.repository.NurseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NursePayrollGenerator {

    private final NurseRepository nurseRepository;
    private final NursePayrollRepository payrollRepository;

    private NursePayroll createPayroll(Nurse nurse, YearMonth month) {

        return NursePayroll.builder()
                .nurseId(nurse.getId())
                .year(month.getYear())
                .month(month.getMonth())
                .status(NursePayrollStatus.DRAFT)
                .compensationType(nurse.getCompensationType())
                .regularAmount(nurse.getCompensationType() == CompensationType.SALARY
                        ? nurse.getBaseSalary()
                        : BigDecimal.ZERO)
                .calculatedRegularAmount(nurse.getCompensationType() == CompensationType.SALARY
                        ? nurse.getBaseSalary()
                        : BigDecimal.ZERO)
                .locked(false)
                .build();
    }

    // To be run at the start of the month
    @Transactional
    public void generatePayrolls(YearMonth yearMonth) {

        LocalDate monthStart = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();

        List<Nurse> nurses = nurseRepository.findAllForPayrollGeneration(monthStart, monthEnd);

        for (Nurse nurse : nurses) {
            if (payrollRepository.existsByNurseIdAndYearAndMonth(nurse.getId(), yearMonth.getYear(), yearMonth.getMonth())) {
                continue;
            }

            payrollRepository.save(createPayroll(nurse, yearMonth));
        }
    }

    // to be run when a new nurse is created or when a nurse's payroll start date is updated
    // TODO: add the start date change
    @Transactional
    public void generateInitialPayrolls(Nurse nurse) {

        YearMonth start = YearMonth.from(nurse.getPayrollStartDate());
        YearMonth current = YearMonth.now();

        while (!start.isAfter(current)) {
            if (!payrollRepository.existsByNurseIdAndYearAndMonth(nurse.getId(), start.getYear(), start.getMonth())) {
                payrollRepository.save(createPayroll(nurse, start));
            }

            start = start.plusMonths(1);
        }
    }

    public NursePayroll getOrCreatePayroll(Nurse nurse, YearMonth month) {
        return payrollRepository
                .findByNurseIdAndYearAndMonth(nurse.getId(), month.getYear(), month.getMonth())
                .orElseGet(() -> payrollRepository.save(createPayroll(nurse, month)));
    }
}