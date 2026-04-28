package com.dentistarchive.utils;

import jakarta.validation.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationErrorUtils {

    public static <T> void validateOrElseThrow(Validator validator, T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object, groups);
        if (isNotEmpty(constraintViolations)) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public static String getFieldName(Path path) {
        String name = null;
        for (Path.Node node : path) {
            if (node.getKind() == ElementKind.PROPERTY) {
                name = node.getName();
            }
        }
        return name;
    }

    public static String getFieldPath(Path path) {
        List<String> names = new ArrayList<>(List.of(""));
        for (Path.Node node : path) {
            if (node.getKind() == ElementKind.PROPERTY) {
                if (node.getIndex() != null) {
                    names.set(names.size() - 1, names.getLast() + "[%d]".formatted(node.getIndex()));
                }
                names.add(node.getName());
            }
        }
        return names.stream()
                .filter(StringUtils::isNotBlank)
                .collect(joining("."));
    }
}
