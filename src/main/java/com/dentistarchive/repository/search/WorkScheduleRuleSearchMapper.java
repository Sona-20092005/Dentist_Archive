package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.WorkScheduleRuleFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.WorkScheduleRuleSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.schedule.QWorkScheduleRule.workScheduleRule;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkScheduleRuleSearchMapper extends SearchMapper<WorkScheduleRuleFilter, WorkScheduleRuleSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(WorkScheduleRuleFilter filter) {
        return PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(workScheduleRule.id, filter.getIds())
                .eq(workScheduleRule.archived, filter.getArchived())
                .eq(workScheduleRule.scheduleId, filter.getScheduleId())
                .inRange(workScheduleRule.createdAt, filter.getCreatedAt())
                .build();
    }

    @Override
    protected Sort.Order toOrder(WorkScheduleRuleSort sortName, SortDirection direction) {
        return switch (sortName) {
            case CREATED_AT -> SortBuilder.buildOrder(workScheduleRule.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(workScheduleRule.updatedAt, direction);
        };
    }

    @Override
    protected Class<WorkScheduleRuleFilter> getFilterClass() {
        return WorkScheduleRuleFilter.class;
    }

}
