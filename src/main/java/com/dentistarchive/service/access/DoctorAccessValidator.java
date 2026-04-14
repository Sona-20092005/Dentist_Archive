package com.dentistarchive.service.access;

import com.dentistarchive.entity.Doctor;
import com.dentistarchive.search.filter.DoctorFilter;
import com.dentistarchive.security.AuthHolder;
import com.dentistarchive.security.AuthUtils;
import com.dentistarchive.security.SecurityManager;
import org.springframework.stereotype.Component;

@Component
public class DoctorAccessValidator extends BaseReadOnlyAccessValidator<Doctor, DoctorFilter> {

    @Override
    protected DoctorFilter buildAccessControlFilter() {
        if (SecurityManager.accessControlDisabled()) {
            return null;
        }
        // TODO: 4/3/2026 implement it later 
//        if (AuthUtils.hasPermission(ActorMsPermission.READ_ALL_USERS.getCode())) {
//            return null;
//        }
        if (AuthHolder.getUserId().isPresent()) {
            return DoctorFilter.byId(AuthHolder.getUserIdOrElseThrow());
        }
        throw AuthUtils.getAccessDeniedException();
    }

    @Override
    protected Class<DoctorFilter> getFilterClass() {
        return DoctorFilter.class;
    }
}
