package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.CompletedTreatmentFilter;
import com.dentistarchive.search.filter.TreatmentPlanFilter;
import com.dentistarchive.search.sort.CompletedTreatmentSort;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.TreatmentPlanSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.patient.QCompletedTreatment.completedTreatment;
import static com.dentistarchive.entity.patient.QPatient.patient;
import static com.dentistarchive.entity.patient.QTreatmentPlan.treatmentPlan;
import static com.dentistarchive.entity.patient.QTreatmentPlanItem.treatmentPlanItem;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompletedTreatmentSearchMapper extends SearchMapper<CompletedTreatmentFilter, CompletedTreatmentSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(CompletedTreatmentFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(completedTreatment.id, filter.getIds())
                .eq(completedTreatment.archived, filter.getArchived())
                .eq(completedTreatment.procedureId, filter.getProcedureId())
                .eq(completedTreatment.treatmentPlanItemId, filter.getTreatmentPlanItemId())
                .eq(completedTreatment.appointmentId, filter.getAppointmentId())
                .eq(completedTreatment.patientId, filter.getPatientId())
                .inRange(completedTreatment.createdAt, filter.getCreatedAt());

        if (filter.getToothNumber() != null) {
            builder.jsonbContainsIgnoreCase(
                    completedTreatment.toothNumbers,
                    filter.getToothNumber().toString()
            );
        }

        if (filter.getDoctorId() != null) {
            builder.and(
                    completedTreatment.patientId.in(
                            select(patient.id)
                                    .from(patient)
                                    .where(patient.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(CompletedTreatmentSort sortName, SortDirection direction) {
        return switch (sortName) {
            case DATE -> SortBuilder.buildOrder(completedTreatment.date, direction);
            case CREATED_AT -> SortBuilder.buildOrder(completedTreatment.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(completedTreatment.updatedAt, direction);
        };
    }

    @Override
    protected Class<CompletedTreatmentFilter> getFilterClass() {
        return CompletedTreatmentFilter.class;
    }

}
