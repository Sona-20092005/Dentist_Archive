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
public class Sort<S extends AvailableSorts> {

    @NotNull
    S sortName;

    @NotNull
    @Schema(description = "Sort direction (ASC - ascending, DESC - descending)", defaultValue = "ASC")
    SortDirection direction = SortDirection.ASC;

    public static <S extends AvailableSorts> Sort<S> of(S sort, SortDirection sortDirection) {
        return new Sort<>(sort, sortDirection);
    }
}
