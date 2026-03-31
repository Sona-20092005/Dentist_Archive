package com.dentistarchive.search.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseFilter<F extends BaseFilter<F>> {

    @Singular
    @ArraySchema(arraySchema = @Schema(description = "Ids (or)"))
    Set<@NotNull UUID> ids;

    @ArraySchema(arraySchema = @Schema(
            description = "Combine filters by 'AND' condition (format of nested filters is the same as this one)",
            example = "[{}]"
    ))
    @JsonProperty("_and_")
    Set<@NotNull F> subFiltersAggregatedByAnd;

    @ArraySchema(arraySchema = @Schema(
            description = "Combine filters by 'OR' condition (format of nested filters is the same as this one)",
            example = "[{}]"
    ))
    @JsonProperty("_or_")
    Set<@NotNull F> subFiltersAggregatedByOr;

    @ArraySchema(arraySchema = @Schema(
            description = "Invert nested filter by 'NOT' operator (format of nested filter is the same as this one)",
            example = "{}"
    ))
    @JsonProperty("_not_")
    F invertedSubFilter;

    @SuppressWarnings("unchecked")
    public F setIds(Set<UUID> ids) {
        this.ids = ids;
        return (F) this;
    }

    @JsonIgnore
    public F setId(UUID id) {
        return setIds(Set.of(id));
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public F and(F filter) {
        if (filter == null) {
            return (F) this;
        }
        F newFilter = (F) getClass().getDeclaredConstructor().newInstance();
        Set<F> subFilters = new HashSet<>();
        subFilters.add((F) this);
        subFilters.add(filter);
        newFilter.setSubFiltersAggregatedByAnd(subFilters);
        return newFilter;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public F or(F filter) {
        if (filter == null) {
            return (F) this;
        }
        F newFilter = (F) getClass().getDeclaredConstructor().newInstance();
        Set<F> subFilters = new HashSet<>();
        subFilters.add((F) this);
        subFilters.add(filter);
        newFilter.setSubFiltersAggregatedByOr(subFilters);
        return newFilter;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public F not() {
        F newFilter = (F) getClass().getDeclaredConstructor().newInstance();
        newFilter.setInvertedSubFilter((F) this);
        return newFilter;
    }
}
