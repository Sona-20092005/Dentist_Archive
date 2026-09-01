package com.dentistarchive.service;

import com.dentistarchive.dto.create.NurseWorkLogCreateDto;
import com.dentistarchive.dto.update.NurseWorkLogUpdateDto;
import com.dentistarchive.entity.nurse.Nurse;
import com.dentistarchive.entity.nurse.NurseWorkLog;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.NurseWorkLogRepository;
import com.dentistarchive.search.filter.NurseWorkLogFilter;
import com.dentistarchive.service.access.NurseWorkLogAccessValidator;
import com.dentistarchive.service.provider.NurseWorkLogProvider;
import com.dentistarchive.validator.NurseWorkLogValidator;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NurseWorkLogService extends BaseReadOnlyService<NurseWorkLog, NurseWorkLogFilter>
        implements ArchivableService<NurseWorkLog, NurseWorkLogFilter> {
    NurseWorkLogRepository nurseWorkLogRepository;
    NurseWorkLogAccessValidator accessValidator;
    NurseWorkLogProvider nurseWorkLogProvider;
    NurseWorkLogValidator nurseWorkLogValidator;
    NurseService nurseService;
    NursePayrollSynchronizer payrollSynchronizer;

    public NurseWorkLogService(
            NurseWorkLogRepository nurseWorkLogRepository,
            NurseWorkLogAccessValidator accessValidator,
            NurseWorkLogProvider nurseWorkLogProvider,
            NurseWorkLogValidator nurseWorkLogValidator,
            NurseService nurseService,
            NursePayrollSynchronizer nursePayrollSynchronizer
    ) {
        super(
                NurseWorkLog.class,
                NurseWorkLogFilter.class,
                nurseWorkLogRepository,
                accessValidator
        );
        this.nurseWorkLogRepository = nurseWorkLogRepository;
        this.accessValidator = accessValidator;
        this.nurseWorkLogProvider = nurseWorkLogProvider;
        this.nurseWorkLogValidator = nurseWorkLogValidator;
        this.nurseService = nurseService;
        this.payrollSynchronizer = nursePayrollSynchronizer;
    }

    @Transactional
    public NurseWorkLog create(NurseWorkLogCreateDto createDto) {
        Nurse nurse = nurseService.getAccessibleNurse(createDto.getNurseId());

        var nurseWorkLog = nurseWorkLogProvider.create(createDto);
        nurseWorkLogValidator.validate(nurseWorkLog, nurse);
        nurseWorkLog = save(nurseWorkLog);

        payrollSynchronizer.applyWorkLogCreate(nurseWorkLog);

        return nurseWorkLog;
    }

    @Transactional
    public NurseWorkLog update(UUID id, @Valid NurseWorkLogUpdateDto updateDto) {

        NurseWorkLog nurseWorkLog = nurseWorkLogRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(NurseWorkLog.class, id));

        accessValidator.validateAccess(nurseWorkLog);

        NurseWorkLog oldWorkLog = copyForPayroll(nurseWorkLog);

        nurseWorkLogProvider.update(nurseWorkLog, updateDto);
        Nurse nurse = nurseService.getAccessibleNurse(nurseWorkLog.getNurseId());
        nurseWorkLogValidator.validateUpdate(nurseWorkLog, nurse);
        nurseWorkLog = nurseWorkLogRepository.save(nurseWorkLog);

        payrollSynchronizer.applyWorkLogUpdate(oldWorkLog, nurseWorkLog);

        return nurseWorkLog;
    }

    @Override
    @Transactional
    public NurseWorkLog unarchiveById(UUID id) {
        NurseWorkLog nurseWorkLog = getByIdOrElseThrow(id);
        validateUnarchive(nurseWorkLog);
        nurseWorkLogValidator.validateUnarchive(nurseWorkLog);
        unarchive(nurseWorkLog);
        nurseWorkLog = save(nurseWorkLog);
        afterUnarchive(nurseWorkLog);
        return nurseWorkLog;
    }

    @Override
    public NurseWorkLog save(NurseWorkLog entity) {
        return nurseWorkLogRepository.save(entity);
    }

    @Override
    public void afterArchive(NurseWorkLog entity) {
        payrollSynchronizer.applyWorkLogDelete(entity);
    }

    @Override
    public void afterUnarchive(NurseWorkLog entity) {
        payrollSynchronizer.applyWorkLogCreate(entity);
    }

    public NurseWorkLog getAccessibleNurseWorkLog(UUID id) {
        if (id == null) {
            return null;
        }

        NurseWorkLog nurseWorkLog = nurseWorkLogRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(NurseWorkLog.class, id));

        accessValidator.validateAccess(nurseWorkLog);

        return nurseWorkLog;
    }

    private NurseWorkLog copyForPayroll(NurseWorkLog nurseWorkLog) {
        NurseWorkLog copy = new NurseWorkLog();

        copy.setId(nurseWorkLog.getId());
        copy.setArchived(nurseWorkLog.isArchived());
        copy.setNurseId(nurseWorkLog.getNurseId());
        copy.setWorkType(nurseWorkLog.getWorkType());
        copy.setDate(nurseWorkLog.getDate());
        copy.setStartTime(nurseWorkLog.getStartTime());
        copy.setEndTime(nurseWorkLog.getEndTime());
        copy.setHourlyRate(nurseWorkLog.getHourlyRate());
        return copy;
    }

}
