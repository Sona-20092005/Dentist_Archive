package com.dentistarchive.service.access;

import com.dentistarchive.entity.Role;
import com.dentistarchive.search.filter.RoleFilter;
import org.springframework.stereotype.Component;

@Component
public class RoleAccessValidator extends BaseReadOnlyAccessValidator<Role, RoleFilter> {

    @Override
    protected RoleFilter buildAccessControlFilter() {
        return null;
    }

    @Override
    protected Class<RoleFilter> getFilterClass() {
        return RoleFilter.class;
    }
}
