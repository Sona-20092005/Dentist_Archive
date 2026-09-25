package com.dentistarchive.service;

import com.dentistarchive.dto.update.RentMonthlyReportUpdateDto;
import com.dentistarchive.entity.rent.RentMonthlyReport;
import com.dentistarchive.repository.RentMonthlyReportRepository;
import com.dentistarchive.search.filter.RentMonthlyReportFilter;
import com.dentistarchive.service.access.RentMonthlyReportAccessValidator;
import com.dentistarchive.service.provider.RentMonthlyReportProvider;
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
public class RentMonthlyReportService extends BaseReadOnlyService<RentMonthlyReport, RentMonthlyReportFilter> {
    RentMonthlyReportRepository rentMonthlyReportRepository;
    RentMonthlyReportAccessValidator accessValidator;
    RentMonthlyReportProvider rentMonthlyReportProvider;
    RentMonthlyReportGenerator rentMonthlyReportGenerator;

    public RentMonthlyReportService(
            RentMonthlyReportRepository rentMonthlyReportRepository,
            RentMonthlyReportAccessValidator accessValidator,
            RentMonthlyReportProvider rentMonthlyReportProvider,
            RentMonthlyReportGenerator rentMonthlyReportGenerator
    ) {
        super(
                RentMonthlyReport.class,
                RentMonthlyReportFilter.class,
                rentMonthlyReportRepository,
                accessValidator
        );
        this.rentMonthlyReportRepository = rentMonthlyReportRepository;
        this.accessValidator = accessValidator;
        this.rentMonthlyReportProvider = rentMonthlyReportProvider;
        this.rentMonthlyReportGenerator = rentMonthlyReportGenerator;
    }

    @Transactional(propagation = Propagation.NEVER)
    public RentMonthlyReport update(UUID id, @Valid RentMonthlyReportUpdateDto updateDto) {

        RentMonthlyReport rentMonthlyReport = getByIdOrElseThrow(id);

        accessValidator.validateAccess(rentMonthlyReport);
        rentMonthlyReportProvider.update(rentMonthlyReport, updateDto);
        return rentMonthlyReportRepository.save(rentMonthlyReport);
    }

    public RentMonthlyReport save(RentMonthlyReport entity) {
        return rentMonthlyReportRepository.save(entity);
    }

    public RentMonthlyReport getAccessibleTechnicianMonthlyReport(UUID id) {
        if (id == null) {
            return null;
        }

        RentMonthlyReport rentMonthlyReport = getByIdOrElseThrow(id);

        accessValidator.validateAccess(rentMonthlyReport);

        return rentMonthlyReport;
    }

    @Transactional
    public void processCurrentMonth(YearMonth month) {
        rentMonthlyReportGenerator.generateReports(month);
    }

}
