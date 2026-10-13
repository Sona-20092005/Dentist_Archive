package com.dentistarchive.service.access;

import com.dentistarchive.entity.material.Material;
import com.dentistarchive.repository.MaterialCategoryRepository;
import com.dentistarchive.search.filter.MaterialFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MaterialAccessValidator extends BaseReadOnlyAccessValidator<Material, MaterialFilter> {
    private final MaterialCategoryRepository categoryRepository;

    @Override
    protected MaterialFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return MaterialFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(Material entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return categoryRepository
                .getDoctorIdByCategoryId(entity.getCategoryId())
                .map(details.getUserId()::equals)
                .orElse(false);
   }
    @Override
    protected Class<MaterialFilter> getFilterClass() {
        return MaterialFilter.class;
    }
}
