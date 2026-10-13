package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.MaterialFilter;
import com.dentistarchive.search.sort.MaterialSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.material.QMaterial.material;
import static com.dentistarchive.entity.material.QMaterialCategory.materialCategory;
import static com.querydsl.jpa.JPAExpressions.select;


@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialSearchMapper extends SearchMapper<MaterialFilter, MaterialSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(MaterialFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(material.id, filter.getIds())
                .eq(material.archived, filter.getArchived())
                .eq(material.categoryId, filter.getCategoryId())
                .containsIgnoreCase(material.name, filter.getNameContains());


        if (filter.getDoctorId() != null) {
            builder.and(
                    material.categoryId.in(
                            select(materialCategory.id)
                                    .from(materialCategory)
                                    .where(materialCategory.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(MaterialSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(material.name, direction);
            case CREATED_AT -> SortBuilder.buildOrder(material.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(material.updatedAt, direction);
        };
    }

    @Override
    protected Class<MaterialFilter> getFilterClass() {
        return MaterialFilter.class;
    }

}
