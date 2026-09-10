package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.TechnicianMonthlyReportFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.TechnicianMonthlyReportSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.technician.QTechnician.technician;
import static com.dentistarchive.entity.technician.QTechnicianMonthlyReport.technicianMonthlyReport;
import static com.querydsl.jpa.JPAExpressions.select;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianMonthlyReportSearchMapper extends SearchMapper<TechnicianMonthlyReportFilter, TechnicianMonthlyReportSort> {

    public TechnicianMonthlyReportSearchMapper() {
    }

    @Override
    protected Predicate toPredicateExceptSubFilters(TechnicianMonthlyReportFilter filter) {
        PredicateBuilder builder = PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(technicianMonthlyReport.id, filter.getIds())
                .in(technicianMonthlyReport.status, filter.getStatuses())
                .eq(technicianMonthlyReport.technicianId, filter.getTechnicianId())
                .inRange(technicianMonthlyReport.createdAt, filter.getCreatedAt());
//                .inRange(technicianMonthlyReport.yearMonth.getYear(), filter.getYear());

        if (filter.getDoctorId() != null) {
            builder.and(
                    technicianMonthlyReport.technicianId.in(
                            select(technician.id)
                                    .from(technician)
                                    .where(technician.doctorId.eq(filter.getDoctorId()))
                    )
            );
        }

        return builder.build();
    }

    @Override
    protected Sort.Order toOrder(TechnicianMonthlyReportSort sortName, SortDirection direction) {
        return switch (sortName) {
            case YEARMONTH -> SortBuilder.buildOrder(technicianMonthlyReport.yearMonth, direction);
            case TOTAL_AMOUNT -> SortBuilder.buildOrder(technicianMonthlyReport.totalAmount, direction);
            case CREATED_AT -> SortBuilder.buildOrder(technicianMonthlyReport.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(technicianMonthlyReport.updatedAt, direction);
        };
    }

    @Override
    protected Class<TechnicianMonthlyReportFilter> getFilterClass() {
        return TechnicianMonthlyReportFilter.class;
    }

}
