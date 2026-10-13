package com.dentistarchive.repository;

import com.dentistarchive.entity.material.Material;
import com.dentistarchive.repository.jpa.MaterialJpaRepository;
import com.dentistarchive.repository.search.MaterialSearchMapper;
import com.dentistarchive.search.filter.MaterialFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialRepository extends BaseReadOnlyRepository<Material, MaterialFilter> {

    MaterialJpaRepository jpaRepository;

    public MaterialRepository(
            MaterialSearchMapper searchMapper,
            MaterialJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Material> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional(readOnly = true)
    public boolean existsByCategoryIdAndArchivedFalse(UUID categoryId) {
        return jpaRepository.existsByCategoryIdAndArchivedFalse(categoryId);
    }

    @Transactional
    public Material save(Material material) {
        return jpaRepository.save(material);
    }





    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
