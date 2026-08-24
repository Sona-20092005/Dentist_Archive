package com.dentistarchive.search.filter;

import com.dentistarchive.enums.CompensationType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NurseCompensationTypeChangeFilter extends BaseFilter<NurseCompensationTypeChangeFilter>{

    Set<CompensationType> compensationTypes;

    UUID nurseId;

    UUID doctorId;

}
