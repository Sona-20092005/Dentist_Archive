package com.dentistarchive.service;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.utils.ClockUtils;

import java.util.UUID;

public interface ArchivableService<E extends ArchivableBaseEntity> {

    E getByIdOrElseThrow(UUID id);

    E save(E entity);

    default void archiveById(UUID id) {
        E entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity);
        save(entity);
    }

    default E unarchiveById(UUID id) {
        E entity = getByIdOrElseThrow(id);
        validateUnarchive(entity);
        unarchive(entity);
        return save(entity);
    }

    default void archive(E entity) {
        entity.setArchived(true);
        entity.setArchivedAt(ClockUtils.now());
        entity.setArchivedBy(getCurrentUserId());
    }

    default void unarchive(E entity) {
        entity.setArchived(false);
        entity.setArchivedAt(null);
        entity.setArchivedBy(null);
    }

    default void validateArchive(E entity) {
        if (getCurrentUserId().equals(entity.getId()) || entity.isArchived()) {
            throw new IllegalStateException(
                    entity.getClass().getSimpleName() + " with id " + entity.getId() + " is already archived"
            );
        }
    }

    default void validateUnarchive(E entity) {
        if (getCurrentUserId().equals(entity.getId()) || !entity.isArchived()) {
            throw new IllegalStateException(
                    entity.getClass().getSimpleName() + " with id " + entity.getId() + " is not archived"
            );
        }
    }

    default UUID getCurrentUserId() {
        return AuthHolder.getUserIdOrElseThrow();
    }

}