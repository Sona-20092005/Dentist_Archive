package com.dentistarchive.service.access;

import com.dentistarchive.entity.material.MaterialPurchase;
import com.dentistarchive.repository.MaterialSupplierRepository;
import com.dentistarchive.repository.SupplierRepository;
import com.dentistarchive.search.filter.MaterialPurchaseFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MaterialPurchaseAccessValidator extends BaseReadOnlyAccessValidator<MaterialPurchase, MaterialPurchaseFilter> {
    private final SupplierRepository supplierRepository;
    private final MaterialSupplierRepository materialSupplierRepository;


    @Override
    protected MaterialPurchaseFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return MaterialPurchaseFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(MaterialPurchase entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return materialSupplierRepository
                .getSupplierIdByMaterialSupplierId(entity.getMaterialSupplierId())
                .flatMap(supplierRepository::getDoctorIdBySupplierId)
                .map(details.getUserId()::equals)
                .orElse(false);
   }
    @Override
    protected Class<MaterialPurchaseFilter> getFilterClass() {
        return MaterialPurchaseFilter.class;
    }
}
