package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.RentMonthlyReportFilter;
import com.dentistarchive.search.sort.RentMonthlyReportSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.QClinic.clinic;
import static com.dentistarchive.entity.rent.QClinicRentAgreement.clinicRentAgreement;
import static com.dentistarchive.entity.rent.QRentMonthlyReport.rentMonthlyReport;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RentMonthlyReportSearchMapper extends SearchMapper<RentMonthlyReportFilter, RentMonthlyReportSort> {

    // TODO: 9/10/2026 add yearMonth filtration 
    @Override
    protected Predicate toPredicateExceptSubFilters(RentMonthlyReportFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(rentMonthlyReport.id, filter.getIds())
                .in(rentMonthlyReport.status, filter.getStatuses())
                .eq(rentMonthlyReport.clinicRentAgreementId, filter.getClinicRentAgreementId())
                .inRange(rentMonthlyReport.createdAt, filter.getCreatedAt());
//                .inRange(technicianMonthlyReport.yearMonth.getYear(), filter.getYear());


        if (filter.getCalculationTypes() != null && !filter.getCalculationTypes().isEmpty()) {
            builder.and(
                    rentMonthlyReport.clinicRentAgreementId.in(
                            select(clinicRentAgreement.id)
                                    .from(clinicRentAgreement)
                                    .where(clinicRentAgreement.rentCalculationType.in(filter.getCalculationTypes()))
                    )
            );
        }

        if (filter.getClinicId() != null) {
            builder.and(
                    rentMonthlyReport.clinicRentAgreementId.in(
                            select(clinicRentAgreement.id)
                                    .from(clinicRentAgreement)
                                    .where(clinicRentAgreement.clinicId.eq(filter.getClinicId()))
                    )
            );
        }

        if (filter.getDoctorId() != null) {
            builder.and(
                    rentMonthlyReport.clinicRentAgreementId.in(
                            select(clinicRentAgreement.id)
                                    .from(clinicRentAgreement)
                                    .where(
                                            clinicRentAgreement.clinicId.in(
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
    protected Sort.Order toOrder(RentMonthlyReportSort sortName, SortDirection direction) {
        return switch (sortName) {
            case YEARMONTH -> SortBuilder.buildOrder(rentMonthlyReport.yearMonth, direction);
            case REGULAR_AMOUNT -> SortBuilder.buildOrder(rentMonthlyReport.regularAmount, direction);
            case OVERTIME_AMOUNT -> SortBuilder.buildOrder(rentMonthlyReport.overtimeAmount, direction);
            case CREATED_AT -> SortBuilder.buildOrder(rentMonthlyReport.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(rentMonthlyReport.updatedAt, direction);
        };
    }

    @Override
    protected Class<RentMonthlyReportFilter> getFilterClass() {
        return RentMonthlyReportFilter.class;
    }

}
