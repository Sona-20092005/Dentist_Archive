package com.dentistarchive.repository;

import com.dentistarchive.entity.nurse.NursePayroll;
import com.dentistarchive.repository.jpa.NursePayrollJpaRepository;
import com.dentistarchive.repository.search.NursePayrollSearchMapper;
import com.dentistarchive.search.filter.NursePayrollFilter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
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
    public Optional<NursePayroll> findByNurseIdAndYearAndMonth(UUID nurseId, Integer year, Month month) {
        return jpaRepository.findByNurseIdAndYearAndMonth(nurseId, year, month);
    }

    @Transactional(readOnly = true)
    public boolean existsByNurseIdAndYearAndMonth(UUID nurseId, Integer year, Month month) {
        return jpaRepository.existsByNurseIdAndYearAndMonth(nurseId, year, month);
    }

    @Transactional(readOnly = true)
    public List<NursePayroll> findAllByNurseIdOrderByYearDescMonthDesc(UUID nurseId) {
        return jpaRepository.findAllByNurseIdOrderByYearDescMonthDesc(nurseId);
    }

    @Transactional(readOnly = true)
    public List<NursePayroll> findAllByYearAndMonth(Integer year, Month month) {
        return jpaRepository.findAllByYearAndMonth(year, month);
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
