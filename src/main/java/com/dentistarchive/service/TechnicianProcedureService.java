package com.dentistarchive.service;

import com.dentistarchive.dto.create.TechnicianProcedureCreateDto;
import com.dentistarchive.dto.update.TechnicianProcedureUpdateDto;
import com.dentistarchive.entity.technician.TechnicianProcedure;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.TechnicianProcedureRepository;
import com.dentistarchive.search.filter.TechnicianProcedureFilter;
import com.dentistarchive.service.access.TechnicianProcedureAccessValidator;
import com.dentistarchive.service.provider.TechnicianProcedureProvider;
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
public class TechnicianProcedureService extends BaseReadOnlyService<TechnicianProcedure, TechnicianProcedureFilter>
        implements ArchivableService<TechnicianProcedure, TechnicianProcedureFilter> {
    TechnicianProcedureRepository procedureRepository;
    TechnicianProcedureAccessValidator accessValidator;
    TechnicianProcedureProvider procedureProvider;
    TechnicianService technicianService;

    public TechnicianProcedureService(
            TechnicianProcedureRepository procedureRepository,
            TechnicianProcedureAccessValidator accessValidator,
            TechnicianProcedureProvider procedureProvider,
            TechnicianService technicianService
    ) {
        super(
                TechnicianProcedure.class,
                TechnicianProcedureFilter.class,
                procedureRepository,
                accessValidator
        );
        this.procedureRepository = procedureRepository;
        this.accessValidator = accessValidator;
        this.procedureProvider = procedureProvider;
        this.technicianService = technicianService;
    }

    @Transactional
    public TechnicianProcedure create(TechnicianProcedureCreateDto createDto) {
        technicianService.getAccessibleTechnician(createDto.getTechnicianId());

        var procedure = procedureProvider.create(createDto);
        return save(procedure);
    }

    @Transactional(propagation = Propagation.NEVER)
    public TechnicianProcedure update(UUID id, @Valid TechnicianProcedureUpdateDto updateDto) {
        TechnicianProcedure procedure = procedureRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(TechnicianProcedure.class, id));

        technicianService.getAccessibleTechnician(updateDto.getTechnicianId());

        accessValidator.validateAccess(procedure);
        procedureProvider.update(procedure, updateDto);
        return procedureRepository.save(procedure);
    }

    @Override
    public TechnicianProcedure save(TechnicianProcedure entity) {
        return procedureRepository.save(entity);
    }

    @Override
    public void afterArchive(TechnicianProcedure entity) {}

    @Override
    public void afterUnarchive(TechnicianProcedure entity) {}

    public TechnicianProcedure getAccessibleTechnicianProcedure(UUID id) {
        if (id == null) {
            return null;
        }

        TechnicianProcedure procedure = procedureRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(TechnicianProcedure.class, id));

        accessValidator.validateAccess(procedure);

        return procedure;
    }

}
