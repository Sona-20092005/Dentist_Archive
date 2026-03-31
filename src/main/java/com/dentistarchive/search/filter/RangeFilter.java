package com.dentistarchive.search.filter;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RangeFilter<T extends Comparable<T>> {
    T min;
    T max;
}
