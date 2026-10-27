package com.dentistarchive.service;

import com.dentistarchive.entity.schedule.WorkSchedule;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.WorkScheduleRepository;
import com.dentistarchive.service.access.WorkScheduleAccessValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleLookupService {
    WorkScheduleRepository workScheduleRepository;
    WorkScheduleAccessValidator workScheduleAccessValidator;

    @Transactional(readOnly = true)
    public WorkSchedule getAccessibleWorkSchedule(UUID id) {
        if (id == null) {
            return null;
        }

        WorkSchedule schedule = workScheduleRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(WorkSchedule.class, id));

        workScheduleAccessValidator.validateAccess(schedule);

        return schedule;
    }
}