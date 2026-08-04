package com.dentistarchive.service;

import com.dentistarchive.dto.create.NurseWorkLogCreateDto;
import com.dentistarchive.dto.update.NurseWorkLogUpdateDto;
import com.dentistarchive.entity.nurse.NurseWorkLog;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.NurseWorkLogRepository;
import com.dentistarchive.search.filter.NurseWorkLogFilter;
import com.dentistarchive.service.access.NurseWorkLogAccessValidator;
import com.dentistarchive.service.provider.NurseWorkLogProvider;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    NurseService nurseService;

    public NurseWorkLogService(
            NurseWorkLogRepository nurseWorkLogRepository,
            NurseWorkLogAccessValidator accessValidator,
            NurseWorkLogProvider nurseWorkLogProvider,
            NurseService nurseService
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
        this.nurseService = nurseService;
    }

    @Transactional
    public NurseWorkLog create(NurseWorkLogCreateDto createDto) {
        nurseService.getAccessibleNurse(createDto.getNurseId());

        var nurseWorkLog = nurseWorkLogProvider.create(createDto);
        return save(nurseWorkLog);
    }

    @Transactional(propagation = Propagation.NEVER)
    public NurseWorkLog update(UUID id, @Valid NurseWorkLogUpdateDto updateDto) {
        nurseService.getAccessibleNurse(updateDto.getNurseId());

        NurseWorkLog nurseWorkLog = nurseWorkLogRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(NurseWorkLog.class, id));

        accessValidator.validateAccess(nurseWorkLog);
        nurseWorkLogProvider.update(nurseWorkLog, updateDto);
        return nurseWorkLogRepository.save(nurseWorkLog);
    }

    @Override
    public NurseWorkLog save(NurseWorkLog entity) {
        return nurseWorkLogRepository.save(entity);
    }

    @Override
    public void afterArchive(NurseWorkLog entity) {}

    @Override
    public void afterUnarchive(NurseWorkLog entity) {}

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

}
