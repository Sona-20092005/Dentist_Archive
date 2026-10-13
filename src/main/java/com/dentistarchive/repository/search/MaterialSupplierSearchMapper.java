package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.MaterialSupplierFilter;
import com.dentistarchive.search.sort.MaterialSupplierSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.material.QSupplier.supplier;
import static com.dentistarchive.entity.material.QMaterialSupplier.materialSupplier;
import static com.querydsl.jpa.JPAExpressions.select;


@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialSupplierSearchMapper extends SearchMapper<MaterialSupplierFilter, MaterialSupplierSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(MaterialSupplierFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(materialSupplier.id, filter.getIds())
                .eq(materialSupplier.archived, filter.getArchived())
                .eq(materialSupplier.materialId, filter.getMaterialId())
                .eq(materialSupplier.supplierId, filter.getSupplierId());


        if (filter.getDoctorId() != null) {
            builder.and(
                    materialSupplier.supplierId.in(
                            select(supplier.id)
                                    .from(supplier)
                                    .where(supplier.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(MaterialSupplierSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(materialSupplier.productName, direction);
            case CREATED_AT -> SortBuilder.buildOrder(materialSupplier.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(materialSupplier.updatedAt, direction);
            case UNIT_PRICE -> SortBuilder.buildOrder(materialSupplier.unitPrice, direction);
            case LAST_PRICE_UPDATE -> SortBuilder.buildOrder(materialSupplier.lastPriceUpdate, direction);


        };
    }

    @Override
    protected Class<MaterialSupplierFilter> getFilterClass() {
        return MaterialSupplierFilter.class;
    }

}
