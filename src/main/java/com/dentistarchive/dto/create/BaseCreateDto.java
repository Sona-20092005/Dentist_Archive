package com.dentistarchive.dto.create;

import com.dentistarchive.dto.BaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseCreateDto<D extends BaseDto> {
}
