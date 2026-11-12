package com.dentistarchive.dto.create;

import com.dentistarchive.dto.SupplierDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class SupplierCreateDto extends BaseCreateDto<SupplierDto>{

    @NotBlank
    String name;

    List<@NotBlank String> phones;

    List<@NotBlank @Email String> emails;

    List<@NotBlank String> addresses;

    String website;

    String notes;

}
