package com.dentistarchive.service.access;

import com.dentistarchive.entity.pricelist.Procedure;
import com.dentistarchive.search.filter.ProcedureFilter;
import org.springframework.stereotype.Component;

@Component
public class ProcedureAccessValidator extends BaseReadOnlyAccessValidator<Procedure, ProcedureFilter> {

    @Override
    protected ProcedureFilter buildAccessControlFilter() {
        return null;
    }

    @Override
    protected boolean hasAccess(Procedure entity) {
//        CustomUserDetails details = AuthHolder.getUserDetailsOrElseThrow();
//
//        return details.getUserId().equals(entity.getDoctorId());
        return true;
    }
    @Override
    protected Class<ProcedureFilter> getFilterClass() {
        return ProcedureFilter.class;
    }
}
