package com.dentistarchive.service;

import com.dentistarchive.dto.create.MaterialPurchaseCreateDto;
import com.dentistarchive.dto.update.MaterialPurchaseUpdateDto;
import com.dentistarchive.entity.material.MaterialPurchase;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.MaterialPurchaseRepository;
import com.dentistarchive.search.filter.MaterialPurchaseFilter;
import com.dentistarchive.service.access.MaterialPurchaseAccessValidator;
import com.dentistarchive.service.provider.MaterialPurchaseProvider;
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
public class MaterialPurchaseService extends BaseReadOnlyService<MaterialPurchase, MaterialPurchaseFilter>
        implements ArchivableService<MaterialPurchase, MaterialPurchaseFilter> {
    MaterialPurchaseRepository materialPurchaseRepository;
    MaterialPurchaseAccessValidator accessValidator;
    MaterialPurchaseProvider materialPurchaseProvider;
    MaterialSupplierService materialSupplierService;

    public MaterialPurchaseService(
            MaterialPurchaseRepository materialPurchaseRepository,
            MaterialPurchaseAccessValidator accessValidator,
            MaterialPurchaseProvider materialPurchaseProvider,
            MaterialSupplierService materialSupplierService
    ) {
        super(
                MaterialPurchase.class,
                MaterialPurchaseFilter.class,
                materialPurchaseRepository,
                accessValidator
        );
        this.materialPurchaseRepository = materialPurchaseRepository;
        this.accessValidator = accessValidator;
        this.materialPurchaseProvider = materialPurchaseProvider;
        this.materialSupplierService = materialSupplierService;
    }

    @Transactional
    public MaterialPurchase create(MaterialPurchaseCreateDto createDto) {
        materialSupplierService.getAccessibleMaterialSupplier(createDto.getMaterialSupplierId());

        var materialPurchase = materialPurchaseProvider.create(createDto);
        materialPurchase = save(materialPurchase);

        return materialPurchase;
    }

    @Transactional(propagation = Propagation.NEVER)
    public MaterialPurchase update(UUID id, @Valid MaterialPurchaseUpdateDto updateDto) {
        materialSupplierService.getAccessibleMaterialSupplier(updateDto.getMaterialSupplierId());

        MaterialPurchase materialPurchase = materialPurchaseRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(MaterialPurchase.class, id));
        accessValidator.validateAccess(materialPurchase);
        materialPurchaseProvider.update(materialPurchase, updateDto);
        return materialPurchaseRepository.save(materialPurchase);
    }

    @Override
    public MaterialPurchase save(MaterialPurchase entity) {
        return materialPurchaseRepository.save(entity);
    }

    @Override
    public void afterArchive(MaterialPurchase entity) {}

    @Override
    public void afterUnarchive(MaterialPurchase entity) {}

    public MaterialPurchase getAccessibleMaterialPurchase(UUID id) {
        if (id == null) {
            return null;
        }

        MaterialPurchase materialPurchase = materialPurchaseRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(MaterialPurchase.class, id));

        accessValidator.validateAccess(materialPurchase);

        return materialPurchase;
    }

}
