package com.dentistarchive.service;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.exception.EntityAlreadyArchivedException;
import com.dentistarchive.exception.EntityNotArchivedException;
import com.dentistarchive.search.filter.BaseFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.service.access.BaseReadOnlyAccessValidator;
import com.dentistarchive.utils.ClockUtils;

import java.util.UUID;

public interface ArchivableService<E extends ArchivableBaseEntity, F extends BaseFilter<F>> {

    BaseReadOnlyAccessValidator<E, F> getAccessValidator();

    E getByIdOrElseThrow(UUID id);

    E save(E entity);

    void afterArchive(E entity);

    void afterUnarchive(E entity);

    default void archiveById(UUID id) {
        E entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity);
        save(entity);
        afterArchive(entity);
    }

    default E unarchiveById(UUID id) {
        E entity = getByIdOrElseThrow(id);
        validateUnarchive(entity);
        unarchive(entity);
        entity = save(entity);
        afterUnarchive(entity);
        return entity;
    }

    default void archive(E entity) {
        entity.setArchived(true);
        entity.setArchivedAt(ClockUtils.now());
        entity.setArchivedBy(AuthHolder.getUserIdOrElseThrow());
    }

    default void unarchive(E entity) {
        entity.setArchived(false);
        entity.setArchivedAt(null);
        entity.setArchivedBy(null);
    }

    default void validateArchive(E entity) {
        getAccessValidator().validateAccess(entity);
        if (entity.isArchived()) {
            throw new EntityAlreadyArchivedException(entity.getId(), entity.getClass());
        }
    }

    default void validateUnarchive(E entity) {
        getAccessValidator().validateAccess(entity);
        if (!entity.isArchived()) {
            throw new EntityNotArchivedException(entity.getId(), entity.getClass());
        }
    }

}