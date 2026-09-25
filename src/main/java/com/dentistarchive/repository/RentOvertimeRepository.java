package com.dentistarchive.repository;

import com.dentistarchive.entity.rent.RentOvertime;
import com.dentistarchive.repository.jpa.RentOvertimeJpaRepository;
import com.dentistarchive.repository.search.RentOvertimeSearchMapper;
import com.dentistarchive.search.filter.RentOvertimeFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RentOvertimeRepository extends BaseReadOnlyRepository<RentOvertime, RentOvertimeFilter> {

    RentOvertimeJpaRepository jpaRepository;

    public RentOvertimeRepository(
            RentOvertimeSearchMapper searchMapper,
            RentOvertimeJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<RentOvertime> findByWorkScheduleModificationId(UUID id) {
        return jpaRepository.findByWorkScheduleModificationId((id));
    }

    @Transactional
    public RentOvertime save(RentOvertime rentOvertime) {
        return jpaRepository.save(rentOvertime);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
