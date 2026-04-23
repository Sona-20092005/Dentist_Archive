package com.dentistarchive.service.access;

import com.dentistarchive.entity.BaseEntity;
import com.dentistarchive.search.filter.BaseFilter;
import lombok.SneakyThrows;
import org.springframework.security.access.AccessDeniedException;

public abstract class BaseReadOnlyAccessValidator<E extends BaseEntity, F extends BaseFilter<F>> {

    /**
     * Returns additional filter restrictions that must always be applied
     * for the current authenticated user.
     *
     * Example:
     * - admin -> empty filter
     * - doctor -> filter with doctorId = currentUserId
     */
    public final F getAccessControlFilter() {
        return buildAccessControlFilter();
    }

    /**
     * Checks access to a concrete entity instance.
     * Should throw AccessDeniedException if access is forbidden.
     */
    public final void validateAccess(E entity) {
        if (entity == null) {
            return;
        }

        if (!hasAccess(entity)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    /**
     * Builds access-control filter for search/list/count/exists operations.
     */
    protected abstract F buildAccessControlFilter();

    /**
     * Checks whether current user may access the конкретный entity.
     */
    protected abstract boolean hasAccess(E entity);

    protected abstract Class<F> getFilterClass();

    @SneakyThrows
    protected F newEmptyFilter() {
        return getFilterClass().getDeclaredConstructor().newInstance();
    }
}