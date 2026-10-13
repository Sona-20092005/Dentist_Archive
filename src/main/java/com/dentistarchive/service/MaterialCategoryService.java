package com.dentistarchive.service;

import com.dentistarchive.dto.create.MaterialCategoryCreateDto;
import com.dentistarchive.dto.update.MaterialCategoryUpdateDto;
import com.dentistarchive.entity.material.MaterialCategory;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.MaterialCategoryRepository;
import com.dentistarchive.repository.MaterialRepository;
import com.dentistarchive.search.filter.MaterialCategoryFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.MaterialCategoryAccessValidator;
import com.dentistarchive.service.provider.MaterialCategoryProvider;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialCategoryService extends BaseReadOnlyService<MaterialCategory, MaterialCategoryFilter>
        implements ArchivableService<MaterialCategory, MaterialCategoryFilter> {
    MaterialCategoryRepository materialCategoryRepository;
    MaterialCategoryAccessValidator accessValidator;
    MaterialCategoryProvider materialCategoryProvider;
    MaterialRepository materialRepository;

    public MaterialCategoryService(
            MaterialCategoryRepository materialCategoryRepository,
            MaterialCategoryAccessValidator accessValidator,
            MaterialCategoryProvider materialCategoryProvider,
            MaterialRepository materialRepository
    ) {
        super(
                MaterialCategory.class,
                MaterialCategoryFilter.class,
                materialCategoryRepository,
                accessValidator
        );
        this.materialCategoryRepository = materialCategoryRepository;
        this.accessValidator = accessValidator;
        this.materialCategoryProvider = materialCategoryProvider;
        this.materialRepository = materialRepository;
    }

    @Transactional
    public MaterialCategory create(MaterialCategoryCreateDto createDto) {

        var materialCategory = materialCategoryProvider.create(createDto, AuthHolder.getUserId().orElseThrow());
        materialCategory = save(materialCategory);

        return materialCategory;
    }

    @Transactional(propagation = Propagation.NEVER)
    public MaterialCategory update(UUID id, @Valid MaterialCategoryUpdateDto updateDto) {
        MaterialCategory materialCategory = materialCategoryRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(MaterialCategory.class, id));
        accessValidator.validateAccess(materialCategory);
        materialCategoryProvider.update(materialCategory, updateDto);
        return materialCategoryRepository.save(materialCategory);
    }

    @Override
    public MaterialCategory save(MaterialCategory entity) {
        return materialCategoryRepository.save(entity);
    }

    @Transactional
    @Override
    public void archiveById(UUID id) {
        MaterialCategory entity = getByIdOrElseThrow(id);
        validateArchive(entity);

        if (materialRepository.existsByCategoryIdAndArchivedFalse(id)) {
            // TODO: 10/6/2026 exception
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Material category cannot be archived while it contains non-archived materials"
            );
        }

        archive(entity);
        save(entity);
        afterArchive(entity);

    }

    @Override
    public void afterArchive(MaterialCategory entity) {}

    @Override
    public void afterUnarchive(MaterialCategory entity) {}

    public MaterialCategory getAccessibleMaterialCategory(UUID id) {
        if (id == null) {
            return null;
        }

        MaterialCategory materialCategory = materialCategoryRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(MaterialCategory.class, id));

        accessValidator.validateAccess(materialCategory);

        return materialCategory;
    }

    public void createDefaultMaterialCategory() {

        MaterialCategory materialCategory = MaterialCategory.builder()
                .name("Default")
                .doctorId(AuthHolder.getUserId().orElseThrow())
                .build();

        materialCategoryRepository.save(materialCategory);
    }

}
