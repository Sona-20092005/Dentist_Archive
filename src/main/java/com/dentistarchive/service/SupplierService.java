package com.dentistarchive.service;

import com.dentistarchive.dto.create.SupplierCreateDto;
import com.dentistarchive.dto.update.SupplierUpdateDto;
import com.dentistarchive.entity.material.Supplier;
import com.dentistarchive.exception.EntityNotFoundByIdException;
import com.dentistarchive.repository.SupplierRepository;
import com.dentistarchive.search.filter.SupplierFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.SupplierAccessValidator;
import com.dentistarchive.service.provider.SupplierProvider;
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
public class SupplierService extends BaseReadOnlyService<Supplier, SupplierFilter>
        implements ArchivableService<Supplier, SupplierFilter> {
    SupplierRepository supplierRepository;
    SupplierAccessValidator accessValidator;
    SupplierProvider supplierProvider;

    public SupplierService(
            SupplierRepository supplierRepository,
            SupplierAccessValidator accessValidator,
            SupplierProvider supplierProvider
    ) {
        super(
                Supplier.class,
                SupplierFilter.class,
                supplierRepository,
                accessValidator
        );
        this.supplierRepository = supplierRepository;
        this.accessValidator = accessValidator;
        this.supplierProvider = supplierProvider;
    }

    @Transactional
    public Supplier create(SupplierCreateDto createDto) {

        var supplier = supplierProvider.create(createDto, AuthHolder.getUserId().orElseThrow());
        supplier = save(supplier);

        return supplier;
    }

    @Transactional(propagation = Propagation.NEVER)
    public Supplier update(UUID id, @Valid SupplierUpdateDto updateDto) {
        Supplier supplier = supplierRepository.getByIdAndNotArchived(id)
                .orElseThrow(() -> new EntityNotFoundByIdException(Supplier.class, id));
        accessValidator.validateAccess(supplier);
        supplierProvider.update(supplier, updateDto);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier save(Supplier entity) {
        return supplierRepository.save(entity);
    }

    @Override
    public void afterArchive(Supplier entity) {}

    @Override
    public void afterUnarchive(Supplier entity) {}

    public Supplier getAccessibleSupplier(UUID id) {
        if (id == null) {
            return null;
        }

        Supplier supplier = supplierRepository
                .getByIdAndNotArchived(id)
                .orElseThrow(() ->
                        new EntityNotFoundByIdException(Supplier.class, id));

        accessValidator.validateAccess(supplier);

        return supplier;
    }

}
