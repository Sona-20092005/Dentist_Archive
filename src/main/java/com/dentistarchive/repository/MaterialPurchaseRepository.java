package com.dentistarchive.repository;

import com.dentistarchive.entity.material.MaterialPurchase;
import com.dentistarchive.repository.jpa.MaterialPurchaseJpaRepository;
import com.dentistarchive.repository.search.MaterialPurchaseSearchMapper;
import com.dentistarchive.search.filter.MaterialPurchaseFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialPurchaseRepository extends BaseReadOnlyRepository<MaterialPurchase, MaterialPurchaseFilter> {

    MaterialPurchaseJpaRepository jpaRepository;

    public MaterialPurchaseRepository(
            MaterialPurchaseSearchMapper searchMapper,
            MaterialPurchaseJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<MaterialPurchase> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional
    public MaterialPurchase save(MaterialPurchase materialPurchase) {
        return jpaRepository.save(materialPurchase);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
