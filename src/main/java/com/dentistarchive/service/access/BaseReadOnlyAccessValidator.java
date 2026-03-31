package com.dentistarchive.service.access;

import com.dentistarchive.search.filter.BaseFilter;
import com.dentistarchive.entity.BaseEntity;
import lombok.SneakyThrows;

import static java.util.Optional.ofNullable;

public abstract class BaseReadOnlyAccessValidator<E extends BaseEntity, F extends BaseFilter<F>> {

    public final F getAccessControlFilter() {
//        if (SecurityManager.accessControlDisabled()) {
//            return newEmptyFilter();
//        }
        return ofNullable(buildAccessControlFilter()).orElseGet(this::newEmptyFilter);
    }

    protected abstract F buildAccessControlFilter();

    protected abstract Class<F> getFilterClass();

    @SneakyThrows
    protected F newEmptyFilter() {
        return getFilterClass().getDeclaredConstructor().newInstance();
    }
}
