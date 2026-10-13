package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.MaterialCategoryFilter;
import com.dentistarchive.search.sort.MaterialCategorySort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.material.QMaterialCategory.materialCategory;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialCategorySearchMapper extends SearchMapper<MaterialCategoryFilter, MaterialCategorySort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(MaterialCategoryFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(materialCategory.id, filter.getIds())
                .eq(materialCategory.archived, filter.getArchived())
                .eq(materialCategory.doctorId, filter.getDoctorId())
                .containsIgnoreCase(materialCategory.name, filter.getNameContains());

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(MaterialCategorySort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(materialCategory.name, direction);
            case CREATED_AT -> SortBuilder.buildOrder(materialCategory.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(materialCategory.updatedAt, direction);
        };
    }

    @Override
    protected Class<MaterialCategoryFilter> getFilterClass() {
        return MaterialCategoryFilter.class;
    }

}
