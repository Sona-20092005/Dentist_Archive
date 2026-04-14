package com.dentistarchive.service;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.utils.ClockUtils;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface ArchivableService<E extends ArchivableBaseEntity> {

    E getByIdOrElseThrow(@NotNull UUID id);

    E save(@NotNull E entity);

    default E archiveById(@NotNull UUID id) {
        E entity = getByIdOrElseThrow(id);
        validateArchive(entity);
        archive(entity);
        return save(entity);
    }

    default E unarchiveById(@NotNull UUID id) {
        E entity = getByIdOrElseThrow(id);
        validateUnarchive(entity);
        unarchive(entity);
        return save(entity);
    }

    default void archive(@NotNull E entity) {
        entity.setArchived(true);
        entity.setArchivedAt(ClockUtils.now());
        entity.setArchivedBy(getCurrentUserId());
    }

    default void unarchive(@NotNull E entity) {
        entity.setArchived(false);
        entity.setArchivedAt(null);
        entity.setArchivedBy(null);
    }

    default void validateArchive(@NotNull E entity) {
        if (entity.isArchived()) {
            throw new IllegalStateException(
                    entity.getClass().getSimpleName() + " with id " + entity.getId() + " is already archived"
            );
        }
    }

    default void validateUnarchive(@NotNull E entity) {
        if (!entity.isArchived()) {
            throw new IllegalStateException(
                    entity.getClass().getSimpleName() + " with id " + entity.getId() + " is not archived"
            );
        }
    }

    default UUID getCurrentUserId() {
        return AuthHolder.getUserIdOrElseThrow();
    }

}