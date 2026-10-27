package com.dentistarchive.service;

import com.dentistarchive.dto.create.CompletedTreatmentCreateDto;
import com.dentistarchive.dto.update.CompletedTreatmentUpdateDto;
import com.dentistarchive.entity.patient.CompletedTreatment;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.CompletedTreatmentRepository;
import com.dentistarchive.repository.PaymentAllocationRepository;
import com.dentistarchive.search.filter.CompletedTreatmentFilter;
import com.dentistarchive.service.access.CompletedTreatmentAccessValidator;
import com.dentistarchive.service.provider.CompletedTreatmentProvider;
import com.dentistarchive.validator.CompletedTreatmentValidator;
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
    CompletedTreatmentValidator treatmentValidator;
    CompletedTreatmentProvider treatmentProvider;
    AppointmentService appointmentService;
    PaymentAllocationRepository paymentAllocationRepository;

    public CompletedTreatmentService(
            PatientService patientService,
            ProcedureService procedureService,
            TreatmentPlanItemService planItemService,
            CompletedTreatmentRepository treatmentRepository,
            CompletedTreatmentAccessValidator accessValidator,
            CompletedTreatmentValidator treatmentValidator,
            CompletedTreatmentProvider treatmentProvider,
            AppointmentService appointmentService,
            PaymentAllocationRepository paymentAllocationRepository

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
        this.treatmentValidator = treatmentValidator;
        this.accessValidator = accessValidator;
        this.treatmentProvider = treatmentProvider;
        this.appointmentService = appointmentService;
        this.paymentAllocationRepository = paymentAllocationRepository;
    }

    @Transactional
    public CompletedTreatment create(@Valid CompletedTreatmentCreateDto createDto) {
        patientService.getAccessiblePatient(createDto.getPatientId());
        procedureService.getAccessibleProcedure(createDto.getProcedureId());
        planItemService.getAccessibleTreatmentPlanItem(createDto.getTreatmentPlanItemId());
        appointmentService.getAccessibleAppointment(createDto.getAppointmentId());

        var treatment = treatmentProvider.create(createDto);
        treatmentValidator.validate(treatment);
        return save(treatment);
    }

    @Transactional(propagation = Propagation.NEVER)
    public CompletedTreatment update(UUID id, @Valid CompletedTreatmentUpdateDto updateDto) {
        procedureService.getAccessibleProcedure(updateDto.getProcedureId());
        planItemService.getAccessibleTreatmentPlanItem(updateDto.getTreatmentPlanItemId());

        CompletedTreatment treatment = treatmentRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(CompletedTreatment.class, id));
        patientService.getAccessiblePatient(treatment.getPatientId());

        accessValidator.validateAccess(treatment);
        treatmentProvider.update(treatment, updateDto);
        treatmentValidator.validate(treatment);
        return treatmentRepository.save(treatment);
    }

    @Override
    public CompletedTreatment save(CompletedTreatment entity) {
        return treatmentRepository.save(entity);
    }

    @Override
    public void archiveById(UUID id) {
        CompletedTreatment entity = getByIdOrElseThrow(id);
        validateArchive(entity);

        if (paymentAllocationRepository.existsByCompletedTreatmentIdAndArchivedFalse(id)) {
            throw new IllegalStateException("Cannot archive completed treatment because it has payment allocations");
        }

        archive(entity);
        save(entity);
        afterArchive(entity);
    }

    @Override
    public void afterArchive(CompletedTreatment entity) {}

    @Override
    public void afterUnarchive(CompletedTreatment entity) {}

    public CompletedTreatment getAccessibleCompletedTreatment(UUID id) {
        if (id == null) {
            return null;
        }

        CompletedTreatment treatment = treatmentRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(CompletedTreatment.class, id));

        accessValidator.validateAccess(treatment);

        return treatment;
    }
    
}
