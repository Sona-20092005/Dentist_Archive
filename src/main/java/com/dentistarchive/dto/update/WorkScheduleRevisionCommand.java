package com.dentistarchive.dto.update;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class WorkScheduleRevisionCommand {

    @NotNull
    LocalDate effectiveFrom;

    @NotEmpty
    List<WorkScheduleRuleRevisionCreateDto> rules;
}