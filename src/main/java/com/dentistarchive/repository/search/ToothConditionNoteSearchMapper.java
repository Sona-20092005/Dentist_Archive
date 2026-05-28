package com.dentistarchive.repository.search;

import com.dentistarchive.search.PredicateBuilder;
import com.dentistarchive.search.SearchMapper;
import com.dentistarchive.search.SortBuilder;
import com.dentistarchive.search.filter.ToothConditionNoteFilter;
import com.dentistarchive.search.sort.SortDirection;
import com.dentistarchive.search.sort.ToothConditionNoteSort;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static com.dentistarchive.entity.patient.QToothConditionNote.toothConditionNote;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ToothConditionNoteSearchMapper extends SearchMapper<ToothConditionNoteFilter, ToothConditionNoteSort> {

    @Override
    protected Predicate toPredicateExceptSubFilters(ToothConditionNoteFilter filter) {
        return PredicateBuilder.builder(PredicateBuilder.Aggregation.AND)
                .in(toothConditionNote.id, filter.getIds())
                .eq(toothConditionNote.patientId, filter.getPatientId())
                .eq(toothConditionNote.toothNumber, filter.getToothNumber())
                .build();
    }

    @Override
    protected Sort.Order toOrder(ToothConditionNoteSort sortName, SortDirection direction) {
        return switch (sortName) {
            case NAME -> SortBuilder.buildOrder(toothConditionNote.toothNumber, direction);
            case CREATED_AT -> SortBuilder.buildOrder(toothConditionNote.createdAt, direction);
            case UPDATED_AT -> SortBuilder.buildOrder(toothConditionNote.updatedAt, direction);
        };
    }

    @Override
    protected Class<ToothConditionNoteFilter> getFilterClass() {
        return ToothConditionNoteFilter.class;
    }

}
