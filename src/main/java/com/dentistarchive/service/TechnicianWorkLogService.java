package com.dentistarchive.service;

import com.dentistarchive.dto.create.TechnicianWorkLogCreateDto;
import com.dentistarchive.dto.update.TechnicianWorkLogUpdateDto;
import com.dentistarchive.entity.technician.TechnicianWorkLog;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.TechnicianWorkLogRepository;
import com.dentistarchive.search.filter.TechnicianWorkLogFilter;
import com.dentistarchive.service.access.TechnicianWorkLogAccessValidator;
import com.dentistarchive.service.provider.TechnicianWorkLogProvider;
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
public class TechnicianWorkLogService extends BaseReadOnlyService<TechnicianWorkLog, TechnicianWorkLogFilter>
        implements ArchivableService<TechnicianWorkLog, TechnicianWorkLogFilter> {
    PatientService patientService;
    TechnicianProcedureService technicianProcedureService;
    TechnicianService technicianService;
    TechnicianWorkLogRepository technicianWorkLogRepository;
    TechnicianWorkLogAccessValidator accessValidator;
    TechnicianWorkLogProvider technicianWorkLogProvider;
    TechnicianMonthlyReportSynchronizer reportSynchronizer;

    public TechnicianWorkLogService(
            PatientService patientService,
            TechnicianProcedureService technicianProcedureService,
            TechnicianService technicianService,
            TechnicianWorkLogRepository technicianWorkLogRepository,
            TechnicianWorkLogAccessValidator accessValidator,
            TechnicianWorkLogProvider technicianWorkLogProvider,
            TechnicianMonthlyReportSynchronizer reportSynchronizer

    ) {
        super(
                TechnicianWorkLog.class,
                TechnicianWorkLogFilter.class,
                technicianWorkLogRepository,
                accessValidator
        );
        this.patientService = patientService;
        this.technicianProcedureService = technicianProcedureService;
        this.technicianService = technicianService;
        this.technicianWorkLogRepository = technicianWorkLogRepository;
        this.accessValidator = accessValidator;
        this.technicianWorkLogProvider = technicianWorkLogProvider;
        this.reportSynchronizer = reportSynchronizer;
    }

    @Transactional
    public TechnicianWorkLog create(TechnicianWorkLogCreateDto createDto) {
        patientService.getAccessiblePatient(createDto.getPatientId());
        technicianProcedureService.getAccessibleTechnicianProcedure(createDto.getTechnicianProcedureId());
        technicianService.getAccessibleTechnician(createDto.getTechnicianId());

        var workLog = technicianWorkLogProvider.create(createDto);
        workLog = save(workLog);

        reportSynchronizer.applyWorkLogCreate(workLog);

        return workLog;
    }

    @Transactional(propagation = Propagation.NEVER)
    public TechnicianWorkLog update(UUID id, @Valid TechnicianWorkLogUpdateDto updateDto) {
        patientService.getAccessiblePatient(updateDto.getPatientId());
        technicianProcedureService.getAccessibleTechnicianProcedure(updateDto.getTechnicianProcedureId());

        TechnicianWorkLog technicianWorkLog = technicianWorkLogRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(TechnicianWorkLog.class, id));

        accessValidator.validateAccess(technicianWorkLog);

        TechnicianWorkLog oldWorkLog = copyForReport(technicianWorkLog);

        technicianWorkLogProvider.update(technicianWorkLog, updateDto);
        technicianWorkLog = technicianWorkLogRepository.save(technicianWorkLog);

        reportSynchronizer.applyWorkLogUpdate(oldWorkLog, technicianWorkLog);

        return technicianWorkLog;

    }

    @Override
    public TechnicianWorkLog save(TechnicianWorkLog entity) {
        return technicianWorkLogRepository.save(entity);
    }


    @Override
    public void afterArchive(TechnicianWorkLog entity) {
        reportSynchronizer.applyWorkLogDelete(entity);
    }

    @Override
    public void afterUnarchive(TechnicianWorkLog entity) {
        reportSynchronizer.applyWorkLogCreate(entity);
    }

    public TechnicianWorkLog getAccessibleTechnicianWorkLog(UUID id) {
        if (id == null) {
            return null;
        }

        TechnicianWorkLog technicianWorkLog = technicianWorkLogRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(TechnicianWorkLog.class, id));

        accessValidator.validateAccess(technicianWorkLog);

        return technicianWorkLog;
    }

    private TechnicianWorkLog copyForReport(TechnicianWorkLog technicianWorkLog) {
        TechnicianWorkLog copy = new TechnicianWorkLog();

        copy.setId(technicianWorkLog.getId());
        copy.setArchived(technicianWorkLog.isArchived());
        copy.setTechnicianId(technicianWorkLog.getTechnicianId());
        copy.setCompletedDate(technicianWorkLog.getCompletedDate());
        copy.setIsPaid(technicianWorkLog.getIsPaid());
        copy.setQuantity(technicianWorkLog.getQuantity());
        copy.setUnitPrice(technicianWorkLog.getUnitPrice());
        return copy;
    }
}
