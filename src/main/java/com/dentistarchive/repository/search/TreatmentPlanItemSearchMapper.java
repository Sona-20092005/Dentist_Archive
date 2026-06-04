package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.TreatmentPlanItemFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.TreatmentPlanItemSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.patient.QPatient.patient;
import static com.dentistarchive.entity.patient.QTreatmentPlan.treatmentPlan;
import static com.dentistarchive.entity.patient.QTreatmentPlanItem.treatmentPlanItem;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanItemSearchMapper extends SearchMapper<TreatmentPlanItemFilter, TreatmentPlanItemSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(TreatmentPlanItemFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(treatmentPlanItem.id, filter.getIds())
                .eq(treatmentPlanItem.archived, filter.getArchived())
                .eq(treatmentPlanItem.procedureId, filter.getProcedureId())
                .eq(treatmentPlanItem.treatmentPlanId, filter.getPlanId())
                .inRange(treatmentPlanItem.createdAt, filter.getCreatedAt());

        if (filter.getToothNumber() != null) {
            builder.jsonbContainsIgnoreCase(
                    treatmentPlanItem.toothNumbers,
                    filter.getToothNumber().toString()
            );
        }

        if (filter.getDoctorId() != null) {

            builder.and(
                    treatmentPlanItem.treatmentPlanId.in(
                            select(treatmentPlan.id)
                                    .from(treatmentPlan)
                                    .where(
                                            treatmentPlan.patientId.in(
                                                    select(patient.id)
                                                            .from(patient)
                                                            .where(patient.doctorId.eq(filter.getDoctorId()))
                                            )
                                    )
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(TreatmentPlanItemSort sortName, SortDirection direction) {
        return switch (sortName) {
            case CREATED_AT -> SortBuilder.buildOrder(treatmentPlanItem.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(treatmentPlanItem.updatedAt, direction);
            case STATUS -> SortBuilder.buildOrder(treatmentPlanItem.itemStatusSortOrder, direction);
        };
    }

    @Override
    protected Class<TreatmentPlanItemFilter> getFilterClass() {
        return TreatmentPlanItemFilter.class;
    }

}
