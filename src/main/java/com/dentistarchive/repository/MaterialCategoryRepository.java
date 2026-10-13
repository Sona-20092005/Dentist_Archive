package com.dentistarchive.repository;

import com.dentistarchive.entity.material.MaterialCategory;
import com.dentistarchive.repository.jpa.MaterialCategoryJpaRepository;
import com.dentistarchive.repository.search.MaterialCategorySearchMapper;
import com.dentistarchive.search.filter.MaterialCategoryFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialCategoryRepository extends BaseReadOnlyRepository<MaterialCategory, MaterialCategoryFilter> {

    MaterialCategoryJpaRepository jpaRepository;

    public MaterialCategoryRepository(
            MaterialCategorySearchMapper searchMapper,
            MaterialCategoryJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<MaterialCategory> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional(readOnly = true)
    public Optional<UUID> getDoctorIdByCategoryId(UUID categoryId) {
        return jpaRepository.findDoctorIdByCategoryId(categoryId);
    }

    @Transactional
    public MaterialCategory save(MaterialCategory materialCategory) {
        return jpaRepository.save(materialCategory);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
