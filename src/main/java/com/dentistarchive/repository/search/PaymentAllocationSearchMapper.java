package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.PaymentAllocationFilter;
import com.dentistarchive.search.sort.PaymentAllocationSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.patient.QPatient.patient;
import static com.dentistarchive.entity.patient.QPayment.payment;
import static com.dentistarchive.entity.patient.QPaymentAllocation.paymentAllocation;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentAllocationSearchMapper extends SearchMapper<PaymentAllocationFilter, PaymentAllocationSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(PaymentAllocationFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(paymentAllocation.id, filter.getIds())
                .eq(paymentAllocation.archived, filter.getArchived())
                .eq(paymentAllocation.paymentId, filter.getPaymentId())
                .eq(paymentAllocation.completedTreatmentId, filter.getCompletedTreatmentId())
                .eq(paymentAllocation.treatmentPlanId, filter.getTreatmentPlanId())
                .inRange(paymentAllocation.createdAt, filter.getCreatedAt());

        if (filter.getDoctorId() != null) {
            builder.and(
                    paymentAllocation.paymentId.in(
                            select(payment.id)
                                    .from(payment)
                                    .where(
                                            payment.patientId.in(
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
    protected Sort.Order toOrder(PaymentAllocationSort sortName, SortDirection direction) {
        return switch (sortName) {
            case CREATED_AT -> SortBuilder.buildOrder(paymentAllocation.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(paymentAllocation.updatedAt, direction);
            case AMOUNT -> SortBuilder.buildOrder(paymentAllocation.amount, direction);
        };
    }

    @Override
    protected Class<PaymentAllocationFilter> getFilterClass() {
        return PaymentAllocationFilter.class;
    }

}
