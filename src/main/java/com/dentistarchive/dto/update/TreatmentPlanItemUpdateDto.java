package com.dentistarchive.dto.update;

import com.dentistarchive.dto.TreatmentPlanItemDto;
import com.dentistarchive.enums.TreatmentPlanItemStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class TreatmentPlanItemUpdateDto extends BaseUpdateDto<TreatmentPlanItemDto> {

    TreatmentPlanItemStatus itemStatus;

    BigDecimal unitPrice;

    List<Integer> toothNumbers;

    String notes;

    UUID procedureId;
}
