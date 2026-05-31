package com.dentistarchive.dto.create;

import com.dentistarchive.dto.TreatmentPlanDto;
import com.dentistarchive.enums.TreatmentPlanStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class TreatmentPlanCreateDto extends BaseCreateDto<TreatmentPlanDto>{

    LocalDate date;

    TreatmentPlanStatus planStatus;

    BigDecimal discountPercent;

    BigDecimal discountAmount;

    String notes;

    UUID patientId;

}
