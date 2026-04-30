package com.dentistarchive.service.access;

import com.dentistarchive.entity.User;
import com.dentistarchive.search.filter.UserFilter;
import com.dentistarchive.security.AuthHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAccessValidator extends BaseReadOnlyAccessValidator<User, UserFilter> {

    @Override
    protected UserFilter buildAccessControlFilter() {
        return newEmptyFilter();
    }

    @Override
    protected boolean hasAccess(User entity) {
        return AuthHolder.getUserIdOrElseThrow().equals(entity.getId());
    }

    @Override
    protected Class<UserFilter> getFilterClass() {
        return UserFilter.class;
    }
}
