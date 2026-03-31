package com.dentistarchive.search.filter;

import com.dentistarchive.search.sort.AvailableSorts;
import com.dentistarchive.search.sort.SortDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchRequest<F, S extends AvailableSorts> {

    @Valid
    F filter;

    @NotNull
    @Valid
    PaginationDto pagination;

    @Valid
    List<SortDto<S>> sorts;

    public SearchRequest(int pageNumber, int pageSize) {
        this(null, pageNumber, pageSize);
    }

    public SearchRequest(F filter, int pageNumber, int pageSize) {
        this.pagination = PaginationDto.builder().pageNumber(pageNumber).pageSize(pageSize).build();
        this.filter = filter;
    }

    public static <F extends BaseFilter<F>> SearchRequest<F, ?> of(F filter) {
        SearchRequest<F, ?> searchRequest = new SearchRequest<>();
        searchRequest.setFilter(filter);
        return searchRequest;
    }

    public static <F extends BaseFilter<F>> SearchRequest<F, ?> of(F filter, int pageNumber, int pageSize) {
        SearchRequest<F, ?> searchRequest = new SearchRequest<>();
        searchRequest.setFilter(filter);
        searchRequest.setPagination(PaginationDto.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build());
        return searchRequest;
    }
}
