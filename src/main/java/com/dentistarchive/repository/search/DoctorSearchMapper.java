package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.DoctorFilter;
import com.dentistarchive.search.sort.DoctorSort;
import com.dentistarchive.search.sort.SortDirection;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.QDoctor.doctor;

@Component
public class DoctorSearchMapper extends SearchMapper<DoctorFilter, DoctorSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(DoctorFilter filter) {
        var builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(doctor.id, filter.getIds())
                .eq(doctor.archived, filter.getArchived())
                .containsIgnoreCase(doctor.fullName, filter.getFullNameContains())
                .in(doctor.email, filter.getEmails())
//                .in(doctor.phone, filter.getPhones())
                .in(doctor.scope, filter.getScopes())
                .in(doctor.companyId, filter.getCompanyIds());

        if (filter.getInCompany() != null) {
            if (filter.getInCompany()) {
                builder.isNotNull(doctor.companyId);
            } else {
                builder.isNull(doctor.companyId);
            }
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(DoctorSort sortName, SortDirection direction) {
        return switch (sortName) {
            case FULL_NAME -> SortBuilder.buildOrder(doctor.fullName, direction);
            case CREATED_AT -> SortBuilder.buildOrder(doctor.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(doctor.updatedAt, direction);
        };
    }

    @Override
    protected Class<DoctorFilter> getFilterClass() {
        return DoctorFilter.class;
    }
}
