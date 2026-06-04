package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.TreatmentPlanFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.TreatmentPlanSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import static com.querydsl.jpa.JPAExpressions.select;

import static com.dentistarchive.entity.patient.QPatient.patient;
import static com.dentistarchive.entity.patient.QTreatmentPlan.treatmentPlan;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TreatmentPlanSearchMapper extends SearchMapper<TreatmentPlanFilter, TreatmentPlanSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(TreatmentPlanFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(treatmentPlan.id, filter.getIds())
                .in(treatmentPlan.planStatus, filter.getStatuses())
                .eq(treatmentPlan.archived, filter.getArchived())
                .eq(treatmentPlan.patientId, filter.getPatientId());

        if (filter.getDoctorId() != null) {
            builder.and(
                    treatmentPlan.patientId.in(
                            select(patient.id)
                                    .from(patient)
                                    .where(patient.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(TreatmentPlanSort sortName, SortDirection direction) {
        return switch (sortName) {
            case DATE -> SortBuilder.buildOrder(treatmentPlan.date, direction);
            case CREATED_AT -> SortBuilder.buildOrder(treatmentPlan.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(treatmentPlan.updatedAt, direction);
        };
    }

    @Override
    protected Class<TreatmentPlanFilter> getFilterClass() {
        return TreatmentPlanFilter.class;
    }

}
