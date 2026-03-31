package com.dentistarchive.search.sort;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SortDto<S extends AvailableSorts> {

    @NotNull
    S sort;

    @NotNull
    @Schema(description = "Sort direction (ASC - ascending, DESC - descending)", defaultValue = "ASC")
    SortDirection direction = SortDirection.ASC;

    public static <S extends AvailableSorts> SortDto<S> of(S sort, SortDirection sortDirection) {
        return new SortDto<>(sort, sortDirection);
    }
}
