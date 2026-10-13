package com.dentistarchive.dto.update;

import com.dentistarchive.dto.SupplierDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@FieldNameConstants
public class SupplierUpdateDto extends BaseUpdateDto<SupplierDto> {

    String name;

    List<String> phones;

    List<String> emails;

    List<String> addresses;

    String website;

    String notes;
}
