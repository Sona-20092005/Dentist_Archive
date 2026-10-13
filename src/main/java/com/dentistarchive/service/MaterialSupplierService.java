package com.dentistarchive.service;

import com.dentistarchive.dto.create.MaterialSupplierCreateDto;
import com.dentistarchive.dto.update.MaterialSupplierUpdateDto;
import com.dentistarchive.entity.material.MaterialSupplier;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.MaterialSupplierRepository;
import com.dentistarchive.search.filter.MaterialSupplierFilter;
import com.dentistarchive.service.access.MaterialSupplierAccessValidator;
import com.dentistarchive.service.provider.MaterialSupplierProvider;
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
public class MaterialSupplierService extends BaseReadOnlyService<MaterialSupplier, MaterialSupplierFilter>
        implements ArchivableService<MaterialSupplier, MaterialSupplierFilter> {
    MaterialSupplierRepository materialSupplierRepository;
    MaterialSupplierAccessValidator accessValidator;
    MaterialSupplierProvider materialSupplierProvider;
    SupplierService supplierService;
    MaterialService materialService;

    public MaterialSupplierService(
            MaterialSupplierRepository materialSupplierRepository,
            MaterialSupplierAccessValidator accessValidator,
            MaterialSupplierProvider materialSupplierProvider,
            SupplierService supplierService,
            MaterialService materialService
    ) {
        super(
                MaterialSupplier.class,
                MaterialSupplierFilter.class,
                materialSupplierRepository,
                accessValidator
        );
        this.materialSupplierRepository = materialSupplierRepository;
        this.accessValidator = accessValidator;
        this.materialSupplierProvider = materialSupplierProvider;
        this.supplierService = supplierService;
        this.materialService = materialService;
    }

    @Transactional
    public MaterialSupplier create(MaterialSupplierCreateDto createDto) {
        supplierService.getAccessibleSupplier(createDto.getSupplierId());
        materialService.getAccessibleMaterial(createDto.getMaterialId());

        var materialSupplier = materialSupplierProvider.create(createDto);
        materialSupplier = save(materialSupplier);

        return materialSupplier;
    }

    @Transactional(propagation = Propagation.NEVER)
    public MaterialSupplier update(UUID id, @Valid MaterialSupplierUpdateDto updateDto) {
        supplierService.getAccessibleSupplier(updateDto.getSupplierId());
        materialService.getAccessibleMaterial(updateDto.getMaterialId());

        MaterialSupplier materialSupplier = materialSupplierRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(MaterialSupplier.class, id));
        accessValidator.validateAccess(materialSupplier);
        materialSupplierProvider.update(materialSupplier, updateDto);
        return materialSupplierRepository.save(materialSupplier);
    }

    @Override
    public MaterialSupplier save(MaterialSupplier entity) {
        return materialSupplierRepository.save(entity);
    }

    @Override
    public void afterArchive(MaterialSupplier entity) {}

    @Override
    public void afterUnarchive(MaterialSupplier entity) {}

    public MaterialSupplier getAccessibleMaterialSupplier(UUID id) {
        if (id == null) {
            return null;
        }

        MaterialSupplier materialSupplier = materialSupplierRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(MaterialSupplier.class, id));

        accessValidator.validateAccess(materialSupplier);

        return materialSupplier;
    }

}
