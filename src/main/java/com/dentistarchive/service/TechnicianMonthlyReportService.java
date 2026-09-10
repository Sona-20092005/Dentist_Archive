package com.dentistarchive.service;

import com.dentistarchive.dto.update.TechnicianMonthlyReportUpdateDto;
import com.dentistarchive.entity.technician.TechnicianMonthlyReport;
import com.dentistarchive.repository.TechnicianMonthlyReportRepository;
import com.dentistarchive.search.filter.TechnicianMonthlyReportFilter;
import com.dentistarchive.service.access.TechnicianMonthlyReportAccessValidator;
import com.dentistarchive.service.provider.TechnicianMonthlyReportProvider;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.YearMonth;
import java.util.UUID;

@Service
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TechnicianMonthlyReportService extends BaseReadOnlyService<TechnicianMonthlyReport, TechnicianMonthlyReportFilter> {
    TechnicianMonthlyReportRepository technicianMonthlyReportRepository;
    TechnicianMonthlyReportAccessValidator accessValidator;
    TechnicianMonthlyReportProvider technicianMonthlyReportProvider;
    TechnicianMonthlyReportGenerator technicianMonthlyReportGenerator;

    public TechnicianMonthlyReportService(
            TechnicianMonthlyReportRepository technicianMonthlyReportRepository,
            TechnicianMonthlyReportAccessValidator accessValidator,
            TechnicianMonthlyReportProvider technicianMonthlyReportProvider,
            TechnicianMonthlyReportGenerator technicianMonthlyReportGenerator
    ) {
        super(
                TechnicianMonthlyReport.class,
                TechnicianMonthlyReportFilter.class,
                technicianMonthlyReportRepository,
                accessValidator
        );
        this.technicianMonthlyReportRepository = technicianMonthlyReportRepository;
        this.accessValidator = accessValidator;
        this.technicianMonthlyReportProvider = technicianMonthlyReportProvider;
        this.technicianMonthlyReportGenerator = technicianMonthlyReportGenerator;
    }

    @Transactional(propagation = Propagation.NEVER)
    public TechnicianMonthlyReport update(UUID id, @Valid TechnicianMonthlyReportUpdateDto updateDto) {

        TechnicianMonthlyReport technicianMonthlyReport = getByIdOrElseThrow(id);

        accessValidator.validateAccess(technicianMonthlyReport);
        technicianMonthlyReportProvider.update(technicianMonthlyReport, updateDto);
        return technicianMonthlyReportRepository.save(technicianMonthlyReport);
    }

    public TechnicianMonthlyReport save(TechnicianMonthlyReport entity) {
        return technicianMonthlyReportRepository.save(entity);
    }

    public TechnicianMonthlyReport getAccessibleTechnicianMonthlyReport(UUID id) {
        if (id == null) {
            return null;
        }

        TechnicianMonthlyReport technicianMonthlyReport = getByIdOrElseThrow(id);

        accessValidator.validateAccess(technicianMonthlyReport);

        return technicianMonthlyReport;
    }

    @Transactional
    public void processCurrentMonth(YearMonth month) {
        technicianMonthlyReportGenerator.generateReports(month);
    }

}
