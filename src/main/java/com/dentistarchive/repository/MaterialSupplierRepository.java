package com.dentistarchive.repository;

import com.dentistarchive.entity.material.MaterialSupplier;
import com.dentistarchive.repository.jpa.MaterialSupplierJpaRepository;
import com.dentistarchive.repository.search.MaterialSupplierSearchMapper;
import com.dentistarchive.search.filter.MaterialSupplierFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialSupplierRepository extends BaseReadOnlyRepository<MaterialSupplier, MaterialSupplierFilter> {

    MaterialSupplierJpaRepository jpaRepository;

    public MaterialSupplierRepository(
            MaterialSupplierSearchMapper searchMapper,
            MaterialSupplierJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<MaterialSupplier> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional(readOnly = true)
    public Optional<UUID> getSupplierIdByMaterialSupplierId(UUID materialSupplierId) {
        return jpaRepository.findSupplierIdByMaterialSupplierId(materialSupplierId);
    }

    @Transactional
    public MaterialSupplier save(MaterialSupplier materialSupplier) {
        return jpaRepository.save(materialSupplier);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
