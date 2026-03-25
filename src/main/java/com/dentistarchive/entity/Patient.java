package com.dentistarchive.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Patient extends ArchivingBaseEntity{

    @NonNull
//    @ColumnInfo(name = "patient_name")
    String patientName;

    String address;

//    @ColumnInfo(name = "passport_information")
    String passportInformation;

    String notes;

}


