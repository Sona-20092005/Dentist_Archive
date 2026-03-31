package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EntityNotFoundByIdException extends EntityNotFoundByFieldException {

    public EntityNotFoundByIdException(Class<?> entityClass, UUID id) {
        this(entityClass, Set.of(id));
    }

    public EntityNotFoundByIdException(Class<?> entityClass, Set<UUID> ids) {
        super(entityClass, "id", ids.stream().map(Object::toString).collect(Collectors.toSet()));
    }
}
