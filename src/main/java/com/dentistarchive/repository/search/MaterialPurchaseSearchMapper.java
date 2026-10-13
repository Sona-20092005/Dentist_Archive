package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.MaterialPurchaseFilter;
import com.dentistarchive.search.sort.MaterialPurchaseSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.material.QMaterialPurchase.materialPurchase;
import static com.dentistarchive.entity.material.QMaterialSupplier.materialSupplier;
import static com.dentistarchive.entity.material.QSupplier.supplier;
import static com.querydsl.jpa.JPAExpressions.select;


@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialPurchaseSearchMapper extends SearchMapper<MaterialPurchaseFilter, MaterialPurchaseSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(MaterialPurchaseFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(materialPurchase.id, filter.getIds())
                .eq(materialPurchase.archived, filter.getArchived())
                .eq(materialPurchase.materialSupplierId, filter.getMaterialSupplierId())
                .inRange(materialPurchase.purchaseDate, filter.getPurchaseDate())
                .inRange(materialPurchase.createdAt, filter.getCreatedAt());

        if (filter.getSupplierId() != null) {
            builder.and(
                    materialPurchase.materialSupplierId.in(
                            select(materialSupplier.id)
                                    .from(materialSupplier)
                                    .where(materialSupplier.supplierId.eq(filter.getSupplierId()))
                    )
            );
        }

        if (filter.getMaterialId() != null) {
            builder.and(
                    materialPurchase.materialSupplierId.in(
                            select(materialSupplier.id)
                                    .from(materialSupplier)
                                    .where(materialSupplier.materialId.eq(filter.getMaterialId()))
                    )
            );
        }

        if (filter.getDoctorId() != null) {
            builder.and(
                    materialPurchase.materialSupplierId.in(
                            select(materialSupplier.id)
                                    .from(materialSupplier)
                                    .where(
                                            materialSupplier.supplierId.in(
                                                    select(supplier.id)
                                                            .from(supplier)
                                                            .where(supplier.doctorId.eq(filter.getDoctorId()))
                                            )
                                    )
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(MaterialPurchaseSort sortName, SortDirection direction) {
        return switch (sortName) {
            case CREATED_AT -> SortBuilder.buildOrder(materialPurchase.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(materialPurchase.updatedAt, direction);
            case PURCHASE_DATE -> SortBuilder.buildOrder(materialPurchase.purchaseDate, direction);
            case TOTAL_AMOUNT -> SortBuilder.buildOrder(materialPurchase.totalAmount, direction);
        };
    }

    @Override
    protected Class<MaterialPurchaseFilter> getFilterClass() {
        return MaterialPurchaseFilter.class;
    }

}
