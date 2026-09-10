package com.dentistarchive.service.access;

import com.dentistarchive.entity.technician.TechnicianProcedure;
import com.dentistarchive.repository.TechnicianRepository;
import com.dentistarchive.search.filter.TechnicianProcedureFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class TechnicianProcedureAccessValidator extends BaseReadOnlyAccessValidator<TechnicianProcedure, TechnicianProcedureFilter> {
    private final TechnicianRepository technicianRepository;

    public TechnicianProcedureAccessValidator(TechnicianRepository technicianRepository) {
        super();
        this.technicianRepository = technicianRepository;
    }

    @Override
    protected TechnicianProcedureFilter buildAccessControlFilter() {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return TechnicianProcedureFilter.builder().
                doctorId(details.getUserId()).
                build();
    }

    @Override
    protected boolean hasAccess(TechnicianProcedure entity) {
        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();

        return technicianRepository
                .getDoctorIdByTechnicianId(entity.getTechnicianId())
                .map(details.getUserId()::equals)
                .orElse(false);
   }
    @Override
    protected Class<TechnicianProcedureFilter> getFilterClass() {
        return TechnicianProcedureFilter.class;
    }
}
