package com.dentistarchive.entity.patient;

import com.dentistarchive.entity.MutableBaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
//@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ToothStatus extends MutableBaseEntity {

    String name;
}


