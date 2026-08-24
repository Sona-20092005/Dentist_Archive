package com.dentistarchive.service;

import com.dentistarchive.dto.update.NursePayrollUpdateDto;
import com.dentistarchive.entity.nurse.NursePayroll;
import com.dentistarchive.repository.NursePayrollRepository;
import com.dentistarchive.repository.NurseRepository;
import com.dentistarchive.search.filter.NursePayrollFilter;
import com.dentistarchive.service.access.NursePayrollAccessValidator;
import com.dentistarchive.service.provider.NursePayrollProvider;
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
public class NursePayrollService extends BaseReadOnlyService<NursePayroll, NursePayrollFilter> {
    NursePayrollRepository nursePayrollRepository;
    NursePayrollAccessValidator accessValidator;
    NursePayrollProvider nursePayrollProvider;
    NursePayrollGenerator payrollGenerator;
    NurseRepository nurseRepository;

    public NursePayrollService(
            NursePayrollRepository nursePayrollRepository,
            NursePayrollAccessValidator accessValidator,
            NursePayrollProvider nursePayrollProvider,
            NursePayrollGenerator payrollGenerator,
            NurseRepository nurseRepository
    ) {
        super(
                NursePayroll.class,
                NursePayrollFilter.class,
                nursePayrollRepository,
                accessValidator
        );
        this.nursePayrollRepository = nursePayrollRepository;
        this.accessValidator = accessValidator;
        this.nursePayrollProvider = nursePayrollProvider;
        this.payrollGenerator = payrollGenerator;
        this.nurseRepository = nurseRepository;
    }

    @Transactional(propagation = Propagation.NEVER)
    public NursePayroll update(UUID id, @Valid NursePayrollUpdateDto updateDto) {

        NursePayroll nursePayroll = getByIdOrElseThrow(id);

        accessValidator.validateAccess(nursePayroll);
        nursePayrollProvider.update(nursePayroll, updateDto);
        return nursePayrollRepository.save(nursePayroll);
    }

    public NursePayroll save(NursePayroll entity) {
        return nursePayrollRepository.save(entity);
    }

    public NursePayroll getAccessibleNursePayroll(UUID id) {
        if (id == null) {
            return null;
        }

        NursePayroll nursePayroll = getByIdOrElseThrow(id);

        accessValidator.validateAccess(nursePayroll);

        return nursePayroll;
    }

}
