package com.dentistarchive.repository;

import com.dentistarchive.entity.nurse.NursePayroll;
import com.dentistarchive.repository.jpa.NursePayrollJpaRepository;
import com.dentistarchive.repository.search.NursePayrollSearchMapper;
import com.dentistarchive.search.filter.NursePayrollFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NursePayrollRepository extends BaseReadOnlyRepository<NursePayroll, NursePayrollFilter> {

    NursePayrollJpaRepository jpaRepository;

    public NursePayrollRepository(
            NursePayrollSearchMapper searchMapper,
            NursePayrollJpaRepository jpaRepository
    ) {
        super(searchMapper, jpaRepository);
        this.jpaRepository = jpaRepository;
    }

    @Transactional(readOnly = true)
    public Optional<NursePayroll> findByNurseIdAndYearAndMonth(UUID nurseId, YearMonth yearMonth) {
        return jpaRepository.findByNurseIdAndYearMonth(nurseId, yearMonth);
    }

    @Transactional(readOnly = true)
    public boolean existsByNurseIdAndYearAndMonth(UUID nurseId, YearMonth yearMonth) {
        return jpaRepository.existsByNurseIdAndYearMonth(nurseId, yearMonth);
    }

    @Transactional(readOnly = true)
    public List<NursePayroll> findAllByNurseIdOrderByYearDescMonthDesc(UUID nurseId) {
        return jpaRepository.findAllByNurseIdOrderByYearMonthDesc(nurseId);
    }

    @Transactional(readOnly = true)
    public List<NursePayroll> findAllByYearAndMonth(YearMonth yearMonth) {
        return jpaRepository.findAllByYearMonth(yearMonth);
    }


    @Transactional
    public NursePayroll save(NursePayroll nursePayroll) {
        return jpaRepository.save(nursePayroll);
    }

    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }
}
