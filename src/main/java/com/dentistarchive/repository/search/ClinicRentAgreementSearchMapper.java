package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.ClinicRentAgreementFilter;
import com.dentistarchive.search.sort.ClinicRentAgreementSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.QClinic.clinic;
import static com.dentistarchive.entity.rent.QClinicRentAgreement.clinicRentAgreement;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClinicRentAgreementSearchMapper extends SearchMapper<ClinicRentAgreementFilter, ClinicRentAgreementSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(ClinicRentAgreementFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(clinicRentAgreement.id, filter.getIds())
                .in(clinicRentAgreement.rentCalculationType, filter.getRentCalculationTypes())
                .eq(clinicRentAgreement.clinicId, filter.getClinicId())
                .inRange(clinicRentAgreement.createdAt, filter.getCreatedAt());


        if (filter.getDoctorId() != null) {
            builder.and(
                    clinicRentAgreement.clinicId.in(
                            select(clinic.id)
                                    .from(clinic)
                                    .where(clinic.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(ClinicRentAgreementSort sortName, SortDirection direction) {
        return switch (sortName) {
            case YEARMONTH -> SortBuilder.buildOrder(clinicRentAgreement.effectiveFrom, direction);
            case CREATED_AT -> SortBuilder.buildOrder(clinicRentAgreement.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(clinicRentAgreement.updatedAt, direction);
        };
    }

    @Override
    protected Class<ClinicRentAgreementFilter> getFilterClass() {
        return ClinicRentAgreementFilter.class;
    }

}
