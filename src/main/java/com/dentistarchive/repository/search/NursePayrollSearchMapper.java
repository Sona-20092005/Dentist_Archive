package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.NursePayrollFilter;
import com.dentistarchive.search.sort.NursePayrollSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.dentistarchive.entity.QClinic.clinic;
import static com.dentistarchive.entity.nurse.QNurse.nurse;
import static com.dentistarchive.entity.nurse.QNursePayroll.nursePayroll;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NursePayrollSearchMapper extends SearchMapper<NursePayrollFilter, NursePayrollSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(NursePayrollFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(nursePayroll.id, filter.getIds())
                .in(nursePayroll.status, filter.getStatuses())
                .in(nursePayroll.compensationType, filter.getCompensationType())
                .eq(nursePayroll.nurseId, filter.getNurseId())
                .inRange(nursePayroll.createdAt, filter.getCreatedAt())
                .inRange(nursePayroll.year, filter.getYear())
                .inRange(nursePayroll.paymentDate, filter.getPaymentDate());

        BooleanExpression manuallyAdjusted =
                nursePayroll.regularAmount.ne(nursePayroll.calculatedRegularAmount)
                        .or(nursePayroll.overtimeAmount.ne(nursePayroll.calculatedOvertimeAmount))
                        .or(nursePayroll.bonus.ne(BigDecimal.ZERO))
                        .or(nursePayroll.deduction.ne(BigDecimal.ZERO));

        if (filter.getIsManuallyAdjusted() != null) {
            builder.and(
                    filter.getIsManuallyAdjusted()
                            ? manuallyAdjusted
                            : manuallyAdjusted.not()
            );
        }

        if (filter.getClinicId() != null) {
            builder.and(
                    nursePayroll.nurseId.in(
                            select(nurse.id)
                                    .from(nurse)
                                    .where(nurse.clinicId.eq(filter.getClinicId()))
                    )
            );
        }

        if (filter.getDoctorId() != null) {
            builder.and(
                    nursePayroll.nurseId.in(
                            select(nurse.id)
                                    .from(nurse)
                                    .where(
                                            nurse.clinicId.in(
                                                    select(clinic.id)
                                                            .from(clinic)
                                                            .where(clinic.doctorId.eq(filter.getDoctorId()))
                                            )
                                    )
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(NursePayrollSort sortName, SortDirection direction) {
        return switch (sortName) {

            case YEAR -> SortBuilder.buildOrder(nursePayroll.year, direction);
            case MONTH -> SortBuilder.buildOrder(nursePayroll.month, direction);
            case PAYMENT_DATE -> SortBuilder.buildOrder(nursePayroll.paymentDate, direction);
            case REGULAR_AMOUNT -> SortBuilder.buildOrder(nursePayroll.regularAmount, direction);
            case OVERTIME_AMOUNT -> SortBuilder.buildOrder(nursePayroll.overtimeAmount, direction);
            case CREATED_AT -> SortBuilder.buildOrder(nursePayroll.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(nursePayroll.updatedAt, direction);
        };
    }

    @Override
    protected Class<NursePayrollFilter> getFilterClass() {
        return NursePayrollFilter.class;
    }

}
