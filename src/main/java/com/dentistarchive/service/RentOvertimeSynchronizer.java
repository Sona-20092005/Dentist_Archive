package com.dentistarchive.service;

import com.dentistarchive.entity.rent.ClinicRentAgreement;
import com.dentistarchive.entity.rent.RentOvertime;
import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.entity.schedule.WorkScheduleModification;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.ClinicRentAgreementRepository;
import com.dentistarchive.repository.RentOvertimeRepository;
import com.dentistarchive.repository.WorkScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class RentOvertimeSynchronizer {

    // TODO: fix the exceptions (everywhere)

    private final ClinicRentAgreementRepository clinicRentAgreementRepository;
    private final RentOvertimeService rentOvertimeService;
    private final RentOvertimeRepository rentOvertimeRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final RentOvertimeGenerator rentOvertimeGenerator;

    @Transactional
    public void applyModificationCreate(WorkScheduleModification modification) {

        RentOvertime overtime = createRentOvertime(modification);

        rentOvertimeRepository.save(overtime);
    }

    @Transactional
    public void applyModificationDelete(WorkScheduleModification modification) {

        RentOvertime overtime = getExistingOvertime(modification);

        validateReportCanBeModified(overtime);

        rentOvertimeService.archive(overtime);
        rentOvertimeRepository.save(overtime);
    }

    // TODO: 10/29/2026 not every modification has a non-null date (e.g. cancel), hence this logic is faulty look through it.
    private RentOvertime createRentOvertime(WorkScheduleModification modification) {
        WorkSchedule schedule = workScheduleRepository
                .getByIdAndNotArchived(modification.getScheduleId())
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(WorkSchedule.class, modification.getScheduleId())
                );

        ClinicRentAgreement agreement = clinicRentAgreementRepository
                .findEffectiveAgreement(schedule.getClinicId(),
                        YearMonth.from(modification.getDate()))
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(ClinicRentAgreement.class, modification.getId())
                );

        return rentOvertimeGenerator.createOvertime(modification, agreement);
    }

    private RentOvertime getExistingOvertime(WorkScheduleModification modification) {

        return rentOvertimeRepository
                .findByWorkScheduleModificationId(modification.getId())
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(RentOvertime.class, modification.getId())
                );
    }

    private void validateReportCanBeModified(RentOvertime overtime) {
        if (overtime.getLocked()) {
            throw new IllegalStateException(
                    "Report " + overtime.getId() + " is locked"
            );
        }
    }


}