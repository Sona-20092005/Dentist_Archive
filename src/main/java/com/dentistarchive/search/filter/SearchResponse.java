package com.dentistarchive.search.filter;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.function.Function;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchResponse<T> {

    @ArraySchema(arraySchema = @Schema(description = "List of elements (result of filtering, sorting and pagination)"))
    List<T> content;

    @Schema(description = "Page number (starts from 0)")
    Integer pageNumber;

    @Schema(description = "Page size")
    Integer pageSize;

    @Schema(description = "Total number of elements matching given filter")
    Long totalElements;

    @Schema(description = "Total number of pages")
    Integer totalPages;

    public <R> SearchResponse<R> map(Function<List<T>, List<R>> contentMapper) {
        List<R> resultContent = contentMapper.apply(content);
        if (content.size() != resultContent.size()) {
            throw new IllegalArgumentException("Can not map SearchResponse.content because content.size changed");
        }
        return SearchResponse.<R>builder()
                .content(resultContent)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .build();
    }
}
