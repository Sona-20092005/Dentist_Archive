package com.dentistarchive.search.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationDto {

    @Schema(description = "Page number (starts from 0)")
    @NotNull
    @PositiveOrZero
    int pageNumber;

    @Schema(description = "Page size", defaultValue = "20")
    @NotNull
    @Min(1) @Max(100)
    @Builder.Default
    int pageSize = 20;

    public static PaginationDto of(int pageNumber, int pageSize) {
        return new PaginationDto(pageNumber, pageSize);
    }
}
