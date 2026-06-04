package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.TreatmentPlanItemStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreatmentPlanItem extends ArchivableBaseEntity {

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "item_status", nullable = false)
    TreatmentPlanItemStatus itemStatus;

    @NotNull
    @Column(name = "item_status_sort_order", nullable = false)
    Integer itemStatusSortOrder;

    @NotNull
    @Column(name = "unit_price", nullable = false)
    BigDecimal unitPrice;

    @Type(JsonType.class)
    @Column(name = "tooth_numbers", columnDefinition = "jsonb")
    List<Integer> toothNumbers;

    String notes;

    @NotNull
    @Column(name = "procedure_id", nullable = false)
    UUID procedureId;

    @NotNull
    @Column(name = "treatment_plan_id", nullable = false)
    UUID treatmentPlanId;
}


