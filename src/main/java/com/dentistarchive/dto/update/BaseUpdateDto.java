package com.dentistarchive.dto.update;

import com.dentistarchive.dto.BaseDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseUpdateDto<D extends BaseDto> {
}
