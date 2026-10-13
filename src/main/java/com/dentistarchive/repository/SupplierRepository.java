package com.dentistarchive.repository;

import com.dentistarchive.entity.material.Supplier;
import com.dentistarchive.repository.jpa.SupplierJpaRepository;
import com.dentistarchive.repository.search.SupplierSearchMapper;
import com.dentistarchive.search.filter.SupplierFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierRepository extends BaseReadOnlyRepository<Supplier, SupplierFilter> {

    SupplierJpaRepository jpaRepository;

    public SupplierRepository(
            SupplierSearchMapper searchMapper,
            SupplierJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Supplier> getByIdAndNotArchived(UUID id) {
        return jpaRepository.findByIdAndArchivedFalse((id));
    }

    @Transactional(readOnly = true)
    public Optional<UUID> getDoctorIdBySupplierId(UUID supplierId) {
        return jpaRepository.findDoctorIdBySupplierId(supplierId);
    }

    @Transactional
    public Supplier save(Supplier supplier) {
        return jpaRepository.save(supplier);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
