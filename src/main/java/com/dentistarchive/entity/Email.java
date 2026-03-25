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
public class Email extends MutableBaseEntity{

    @NonNull
    public String email;

    //one to many
    @NonNull
//    @ColumnInfo(name = "patient_id")
    public Long patientId;

}


