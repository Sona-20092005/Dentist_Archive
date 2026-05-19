package com.dentistarchive.service;

import com.dentistarchive.dto.create.ProcedureCreateDto;
import com.dentistarchive.dto.update.ProcedureUpdateDto;
import com.dentistarchive.entity.patient.Patient;
import com.dentistarchive.entity.pricelist.Procedure;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.ProcedureRepository;
import com.dentistarchive.search.filter.ProcedureFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.ProcedureAccessValidator;
import com.dentistarchive.service.provider.ProcedureProvider;
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
public class ProcedureService extends BaseReadOnlyService<Procedure, ProcedureFilter>
        implements ArchivableService<Procedure, ProcedureFilter> {
    ProcedureRepository procedureRepository;
    ProcedureAccessValidator accessValidator;
    ProcedureProvider procedureProvider;

    public ProcedureService(
            ProcedureRepository procedureRepository,
            ProcedureAccessValidator accessValidator,
            ProcedureProvider procedureProvider
    ) {
        super(
                Procedure.class,
                ProcedureFilter.class,
                procedureRepository,
                accessValidator
        );
        this.procedureRepository = procedureRepository;
        this.accessValidator = accessValidator;
        this.procedureProvider = procedureProvider;
    }

    @Transactional
    public Procedure create(ProcedureCreateDto createDto) {
        var procedure = procedureProvider.create(createDto, AuthHolder.getUserId().orElseThrow());
        return save(procedure);
    }

    @Transactional(propagation = Propagation.NEVER)
    public Procedure update(UUID id, @Valid ProcedureUpdateDto updateDto) {
        Procedure procedure = procedureRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Patient.class, id));
        accessValidator.validateAccess(procedure);
        procedureProvider.update(procedure, updateDto);
        return procedureRepository.save(procedure);
    }



    @Override
    public Procedure save(Procedure entity) {
        return procedureRepository.save(entity);
    }

    @Override
    public void afterArchive(Procedure entity) {}

    @Override
    public void afterUnarchive(Procedure entity) {}

}
