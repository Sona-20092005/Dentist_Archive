package com.dentistarchive.service;

import com.dentistarchive.entity.rent.ClinicRentAgreement;
import com.dentistarchive.entity.rent.RentOvertime;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.repository.RentOvertimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentOvertimeGenerator {

    private final RentOvertimeRepository rentOvertimeRepository;

    private RentOvertime createOvertimePrivate(WorkScheduleModification modification, ClinicRentAgreement agreement) {

        return RentOvertime.builder()
                .workScheduleModificationId(modification.getId())
                .hourlyRent(agreement.getOvertimeHourlyRate())
                .locked(false)
                .build();
    }

//    // To be run at the start of the month
//    @Transactional
//    public void generatePayrolls(YearMonth yearMonth) {
//
//        LocalDate monthStart = yearMonth.atDay(1);
//        LocalDate monthEnd = yearMonth.atEndOfMonth();
//
//        List<Nurse> nurses = nurseRepository.findAllForPayrollGeneration(monthStart, monthEnd);
//
//        for (Nurse nurse : nurses) {
//            if (payrollRepository.existsByNurseIdAndYearAndMonth(nurse.getId(), yearMonth.getYear(), yearMonth.getMonth())) {
//                continue;
//            }
//
//            payrollRepository.save(createPayroll(nurse, yearMonth));
//        }
//    }

    public RentOvertime createOvertime(WorkScheduleModification modification, ClinicRentAgreement agreement) {
        return createOvertimePrivate(modification, agreement);
    }
}