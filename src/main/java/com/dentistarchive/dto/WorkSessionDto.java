package com.dentistarchive.dto;

import com.dentistarchive.enums.WorkSessionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkSessionDto {
    LocalDate date;

    LocalTime startTime;

    LocalTime endTime;

    WorkSessionType workSessionType;

    UUID scheduleId;

    UUID modificationId;
}
