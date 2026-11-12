package com.dentistarchive.entity.nurse;

import com.dentistarchive.entity.ArchivableBaseEntity;
import com.dentistarchive.enums.CompensationType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Nurse extends ArchivableBaseEntity {

    @NotBlank
    String name;

    @PositiveOrZero
    @Column(name = "base_salary")
    BigDecimal baseSalary;

    @PositiveOrZero
    @Column(name = "hourly_rate")
    BigDecimal hourlyRate;

    @PositiveOrZero
    @Column(name = "overtime_hourly_rate")
    BigDecimal overtimeHourlyRate;

    @Positive
    @Column(name = "contracted_weekly_hours")
    Integer contractedWeeklyHours;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "compensation_type")
    CompensationType compensationType;

    @Column(name = "hire_date")
    LocalDate hireDate;

    @Column(name = "termination_date")
    LocalDate terminationDate;

    @NotNull
    @Column(name = "payroll_start_date")
    LocalDate payrollStartDate;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<@NotBlank String> phones;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    List<@NotBlank @Email String> emails;

    String notes;

    @NotNull
    @Column(name = "clinic_id", nullable = false)
    UUID clinicId;
}

// TODO: 9/4/2026 combine year and month into a single field


