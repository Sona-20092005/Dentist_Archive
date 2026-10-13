package com.dentistarchive.service.access;

import com.dentistarchive.entity.material.Supplier;
import com.dentistarchive.search.filter.SupplierFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class SupplierAccessValidator extends BaseReadOnlyAccessValidator<Supplier, SupplierFilter> {

    @Override
    protected SupplierFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return SupplierFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(Supplier entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return details.getUserId().equals(entity.getDoctorId());
    }

    @Override
    protected Class<SupplierFilter> getFilterClass() {
        return SupplierFilter.class;
    }
}
