package com.dentistarchive.service;

import com.dentistarchive.dto.update.RentOvertimeUpdateDto;
import com.dentistarchive.entity.rent.RentOvertime;
import com.dentistarchive.repository.RentOvertimeRepository;
import com.dentistarchive.search.filter.RentOvertimeFilter;
import com.dentistarchive.service.access.RentOvertimeAccessValidator;
import com.dentistarchive.service.provider.RentOvertimeProvider;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RentOvertimeService extends BaseReadOnlyService<RentOvertime, RentOvertimeFilter>
        implements ArchivableService<RentOvertime, RentOvertimeFilter> {
    RentOvertimeRepository rentOvertimeRepository;
    RentOvertimeAccessValidator accessValidator;
    RentOvertimeProvider rentOvertimeProvider;
    public RentOvertimeService(
            RentOvertimeRepository rentOvertimeRepository,
            RentOvertimeAccessValidator accessValidator,
            RentOvertimeProvider rentOvertimeProvider
    ) {
        super(
                RentOvertime.class,
                RentOvertimeFilter.class,
                rentOvertimeRepository,
                accessValidator
        );
        this.rentOvertimeRepository = rentOvertimeRepository;
        this.accessValidator = accessValidator;
        this.rentOvertimeProvider = rentOvertimeProvider;
   }

    @Transactional(propagation = Propagation.NEVER)
    public RentOvertime update(UUID id, @Valid RentOvertimeUpdateDto updateDto) {

        RentOvertime rentOvertime = getByIdOrElseThrow(id);

        accessValidator.validateAccess(rentOvertime);
        rentOvertimeProvider.update(rentOvertime, updateDto);
        return rentOvertimeRepository.save(rentOvertime);
    }

    @Override
    public void afterArchive(RentOvertime entity) {}

    @Override
    public void afterUnarchive(RentOvertime entity) {}

    @Override
    public RentOvertime save(RentOvertime entity) {
        return rentOvertimeRepository.save(entity);
    }

    public RentOvertime getAccessibleRentOvertime(UUID id) {
        if (id == null) {
            return null;
        }

        RentOvertime rentOvertime = getByIdOrElseThrow(id);

        accessValidator.validateAccess(rentOvertime);

        return rentOvertime;
    }
}
