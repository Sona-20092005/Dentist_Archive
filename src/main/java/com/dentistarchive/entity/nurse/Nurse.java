package com.dentistarchive.entity.nurse;

import com.dentistarchive.entity.ArchivableBaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Nurse extends ArchivableBaseEntity {

    @NotNull
    String name;

//    @Column(name = "base_salary")
    BigDecimal baseSalary;

//    @Column(name = "overtime_hourly_rate")
    BigDecimal overtimeHourlyRate;

    // TODO: 3/30/2026 change to appropriate type
    @NonNull
//    @Column(name = "hire_date")
    LocalDate hireDate;

//    @Column(name = "termination_date")
    LocalDate terminationDate;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<String> phones;

    String notes;
}



