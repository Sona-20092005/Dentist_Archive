package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.PaymentFilter;
import com.dentistarchive.search.sort.PaymentSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.patient.QPatient.patient;
import static com.dentistarchive.entity.patient.QPayment.payment;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentSearchMapper extends SearchMapper<PaymentFilter, PaymentSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(PaymentFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(payment.id, filter.getIds())
                .in(payment.paymentMethod, filter.getMethods())
                .eq(payment.archived, filter.getArchived())
                .inRange(payment.createdAt, filter.getCreatedAt());

        if (filter.getDoctorId() != null) {
            builder.and(
                    payment.patientId.in(
                            select(patient.id)
                                    .from(patient)
                                    .where(patient.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(PaymentSort sortName, SortDirection direction) {
        return switch (sortName) {
            case CREATED_AT -> SortBuilder.buildOrder(payment.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(payment.updatedAt, direction);
            case DATE -> SortBuilder.buildOrder(payment.date, direction);
            case AMOUNT -> SortBuilder.buildOrder(payment.amount, direction);
        };
    }

    @Override
    protected Class<PaymentFilter> getFilterClass() {
        return PaymentFilter.class;
    }

}
