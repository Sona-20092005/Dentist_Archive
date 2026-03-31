package com.dentistarchive.mapper;

import com.dentistarchive.search.filter.SearchResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EntityMapper<E, D> {

    D toDto(E entity);

    default List<D> toDtoList(List<E> entities) {
        return entities.stream().map(this::toDto).toList();
    }

    default SearchResponse<D> toSearchResponse(Page<E> entities) {
        List<D> content = entities.getContent().stream().map(this::toDto).toList();
        SearchResponse<D> searchResponse = new SearchResponse<>();
        searchResponse.setContent(content);
        if (entities.getPageable().isPaged()) {
            searchResponse.setPageSize(entities.getPageable().getPageSize());
            searchResponse.setPageNumber(entities.getPageable().getPageNumber());
            searchResponse.setTotalElements(entities.getTotalElements());
            searchResponse.setTotalPages(entities.getTotalPages());
        }
        return searchResponse;
    }
}
