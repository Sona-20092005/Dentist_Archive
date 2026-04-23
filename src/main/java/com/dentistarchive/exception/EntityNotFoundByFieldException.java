package com.dentistarchive.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EntityNotFoundByFieldException extends CommonErrorCodeException {

    Class<?> entityClass;
    String fieldName;
    Set<String> values;

    public EntityNotFoundByFieldException(Class<?> entityClass, String fieldName, String value) {
        this(entityClass, fieldName, Set.of(value));
    }

    public EntityNotFoundByFieldException(Class<?> entityClass, String fieldName, Set<String> values) {
        this.entityClass = entityClass;
        this.fieldName = fieldName;
        this.values = values;
    }

    @Override
    public String getMessage() {
        String entityName = entityClass.getSimpleName();

        if (values.size() > 1) {
            return String.format(
                    "%s not found by field '%s' with values: %s",
                    entityName,
                    fieldName,
                    String.join(", ", values)
            );
        }

        return String.format(
                "%s not found by field '%s' with value: %s",
                entityName,
                fieldName,
                values.iterator().next()
        );
    }

    @Override
    public String getErrorCode() {
        return values.size() > 1
                ? ErrorCode.ENTITIES_NOT_FOUND.getCode()
                : ErrorCode.ENTITY_NOT_FOUND.getCode();
    }
}
