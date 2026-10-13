package com.dentistarchive.service.access;

import com.dentistarchive.entity.material.MaterialSupplier;
import com.dentistarchive.repository.SupplierRepository;
import com.dentistarchive.search.filter.MaterialSupplierFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MaterialSupplierAccessValidator extends BaseReadOnlyAccessValidator<MaterialSupplier, MaterialSupplierFilter> {
    private final SupplierRepository supplierRepository;

    @Override
    protected MaterialSupplierFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return MaterialSupplierFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(MaterialSupplier entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return supplierRepository
                .getDoctorIdBySupplierId(entity.getSupplierId())
                .map(details.getUserId()::equals)
                .orElse(false);
   }
    @Override
    protected Class<MaterialSupplierFilter> getFilterClass() {
        return MaterialSupplierFilter.class;
    }
}
