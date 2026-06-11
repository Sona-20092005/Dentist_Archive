package com.dentistarchive.service;

import com.dentistarchive.dto.create.CompletedTreatmentCreateDto;
import com.dentistarchive.dto.update.CompletedTreatmentUpdateDto;
import com.dentistarchive.entity.patient.CompletedTreatment;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.CompletedTreatmentRepository;
import com.dentistarchive.search.filter.CompletedTreatmentFilter;
import com.dentistarchive.service.access.CompletedTreatmentAccessValidator;
import com.dentistarchive.service.provider.CompletedTreatmentProvider;
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
public class CompletedTreatmentService extends BaseReadOnlyService<CompletedTreatment, CompletedTreatmentFilter>
        implements ArchivableService<CompletedTreatment, CompletedTreatmentFilter> {
    PatientService patientService;
    ProcedureService procedureService;
    TreatmentPlanItemService planItemService;
    CompletedTreatmentRepository treatmentRepository;
    CompletedTreatmentAccessValidator accessValidator;
    CompletedTreatmentProvider treatmentProvider;

    public CompletedTreatmentService(
            PatientService patientService,
            ProcedureService procedureService,
            TreatmentPlanItemService planItemService,
            CompletedTreatmentRepository treatmentRepository,
            CompletedTreatmentAccessValidator accessValidator,
            CompletedTreatmentProvider treatmentProvider

    ) {
        super(
                CompletedTreatment.class,
                CompletedTreatmentFilter.class,
                treatmentRepository,
                accessValidator
        );
        this.patientService = patientService;
        this.procedureService = procedureService;
        this.planItemService = planItemService;
        this.treatmentRepository = treatmentRepository;
        this.accessValidator = accessValidator;
        this.treatmentProvider = treatmentProvider;
    }

    // TODO: 6/9/2026 Add also getaccesible check for appointment (in create and update)
    @Transactional
    public CompletedTreatment create(CompletedTreatmentCreateDto createDto) {
        patientService.getAccessiblePatient(createDto.getPatientId());
        procedureService.getAccessibleProcedure(createDto.getProcedureId());
        planItemService.getAccessibleTreatmentPlanItem(createDto.getTreatmentPlanItemId());

        var treatment = treatmentProvider.create(createDto);
        return save(treatment);
    }

    @Transactional(propagation = Propagation.NEVER)
    public CompletedTreatment update(UUID id, @Valid CompletedTreatmentUpdateDto updateDto) {
        procedureService.getAccessibleProcedure(updateDto.getProcedureId());
        planItemService.getAccessibleTreatmentPlanItem(updateDto.getTreatmentPlanItemId());

        CompletedTreatment treatment = treatmentRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(CompletedTreatment.class, id));
        accessValidator.validateAccess(treatment);
        treatmentProvider.update(treatment, updateDto);
        return treatmentRepository.save(treatment);
    }

    @Override
    public CompletedTreatment save(CompletedTreatment entity) {
        return treatmentRepository.save(entity);
    }

    @Override
    public void afterArchive(CompletedTreatment entity) {}

    @Override
    public void afterUnarchive(CompletedTreatment entity) {}

}
