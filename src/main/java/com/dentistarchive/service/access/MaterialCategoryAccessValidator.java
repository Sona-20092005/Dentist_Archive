package com.dentistarchive.service.access;

import com.dentistarchive.entity.material.MaterialCategory;
import com.dentistarchive.search.filter.MaterialCategoryFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class MaterialCategoryAccessValidator extends BaseReadOnlyAccessValidator<MaterialCategory, MaterialCategoryFilter> {

    @Override
    protected MaterialCategoryFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return MaterialCategoryFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(MaterialCategory entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return details.getUserId().equals(entity.getDoctorId());
    }

    @Override
    protected Class<MaterialCategoryFilter> getFilterClass() {
        return MaterialCategoryFilter.class;
    }
}
