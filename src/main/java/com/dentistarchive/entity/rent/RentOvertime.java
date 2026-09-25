package com.dentistarchive.entity.rent;

import com.dentistarchive.entity.ArchivableBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RentOvertime extends ArchivableBaseEntity {

    @NonNull
    @Column(name = "work_schedule_modification_id", nullable = false)
    UUID workScheduleModificationId;

    @NonNull
    @Column(name = "hourly_rent", nullable = false)
    BigDecimal hourlyRent;

    @NonNull
    @Column(nullable = false)
    Boolean locked;

    String notes;
}



