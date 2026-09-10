package com.dentistarchive.dto.create;

import com.dentistarchive.dto.TechnicianWorkLogDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class TechnicianWorkLogCreateDto extends BaseCreateDto<TechnicianWorkLogDto>{

    UUID technicianId;

    LocalDate requestedDate;

    LocalDate completedDate;

    BigDecimal unitPrice;

    Integer quantity;

    List<Integer> toothNumbers;

    UUID technicianProcedureId;

    UUID patientId;

    Boolean isPaid;

    LocalDate paymentDate;

    String notes;

}
