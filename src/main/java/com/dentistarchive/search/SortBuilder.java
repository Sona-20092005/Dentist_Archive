package com.dentistarchive.search;

import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SortBuilder {

    public static Sort build(Path<?> path, SortDirection direction) {
        return Sort.by(buildOrder(path, direction));
    }

    public static Sort.Order buildOrder(Path<?> path, SortDirection direction) {
        String propertyPath = path.toString().substring(path.toString().indexOf(".") + 1);
        return buildOrder(propertyPath, direction);
    }

    public static Sort.Order buildOrder(String propertyPath, SortDirection direction) {
        return direction.equals(SortDirection.ASC)
                ? Sort.Order.asc(propertyPath)
                : Sort.Order.desc(propertyPath);
    }
}
