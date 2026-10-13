package com.dentistarchive.service;

import com.dentistarchive.dto.create.MaterialCreateDto;
import com.dentistarchive.dto.update.MaterialUpdateDto;
import com.dentistarchive.entity.material.Material;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.MaterialRepository;
import com.dentistarchive.search.filter.MaterialFilter;
import com.dentistarchive.service.access.MaterialAccessValidator;
import com.dentistarchive.service.provider.MaterialProvider;
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
public class MaterialService extends BaseReadOnlyService<Material, MaterialFilter>
        implements ArchivableService<Material, MaterialFilter> {
    MaterialRepository materialRepository;
    MaterialAccessValidator accessValidator;
    MaterialProvider materialProvider;
    MaterialCategoryService materialCategoryService;

    public MaterialService(
            MaterialRepository materialRepository,
            MaterialAccessValidator accessValidator,
            MaterialProvider materialProvider,
            MaterialCategoryService materialCategoryService
    ) {
        super(
                Material.class,
                MaterialFilter.class,
                materialRepository,
                accessValidator
        );
        this.materialRepository = materialRepository;
        this.accessValidator = accessValidator;
        this.materialProvider = materialProvider;
        this.materialCategoryService = materialCategoryService;
    }

    @Transactional
    public Material create(MaterialCreateDto createDto) {
        materialCategoryService.getAccessibleMaterialCategory(createDto.getCategoryId());
        var material = materialProvider.create(createDto);
        material = save(material);

        return material;
    }

    @Transactional(propagation = Propagation.NEVER)
    public Material update(UUID id, @Valid MaterialUpdateDto updateDto) {
        materialCategoryService.getAccessibleMaterialCategory(updateDto.getCategoryId());
        Material material = materialRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Material.class, id));
        accessValidator.validateAccess(material);
        materialProvider.update(material, updateDto);
        return materialRepository.save(material);
    }

    @Override
    public Material save(Material entity) {
        return materialRepository.save(entity);
    }

    @Override
    public void afterArchive(Material entity) {}

    @Override
    public void afterUnarchive(Material entity) {}

    public Material getAccessibleMaterial(UUID id) {
        if (id == null) {
            return null;
        }

        Material material = materialRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Material.class, id));

        accessValidator.validateAccess(material);

        return material;
    }

}
