package com.dentistarchive.validator;

import com.dentistarchive.entity.patient.CompletedTreatment;
import com.dentistarchive.service.AppointmentService;
import com.dentistarchive.service.TreatmentPlanItemService;
import com.dentistarchive.service.TreatmentPlanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import static com.dentistarchive.utils.ToothNumberValidator.assertThatToothNumberIsValid;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class CompletedTreatmentValidator {
    AppointmentService appointmentService;
    TreatmentPlanItemService treatmentPlanItemService;
    TreatmentPlanService treatmentPlanService;

    public void validate(CompletedTreatment completedTreatment) {
        validateToothNumbers(completedTreatment);
        validateAmount(completedTreatment);
        validatePatient(completedTreatment);
    }

    private void validateToothNumbers(CompletedTreatment completedTreatment) {
        if (completedTreatment.getToothNumbers() != null) {
            for (Integer toothNumber : completedTreatment.getToothNumbers()) {
                assertThatToothNumberIsValid(toothNumber);
            }
        }
    }

    private void validateAmount(CompletedTreatment completedTreatment) {
        if(completedTreatment.getTreatmentPlanItemId() == null && completedTreatment.getUnitPrice() == null) {
            throw new IllegalArgumentException("completed treatments without treatment plan item should have a price");
        }
        if(completedTreatment.getTreatmentPlanItemId() != null && completedTreatment.getUnitPrice() != null) {
            throw new IllegalArgumentException("completed treatments with treatment plan item should not have a price");
        }
    }

    private void validatePatient(CompletedTreatment completedTreatment) {
        if(completedTreatment.getAppointmentId() != null) {
            var appointment = appointmentService.getAccessibleAppointment(completedTreatment.getAppointmentId());
            if(!appointment.getPatientId().equals(completedTreatment.getPatientId())) {
                throw new IllegalArgumentException("appointment and completed treatment patients don't match");
            }
        }
        if(completedTreatment.getTreatmentPlanItemId() != null) {
            var item = treatmentPlanItemService.getAccessibleTreatmentPlanItem(completedTreatment.getTreatmentPlanItemId());
            var plan = treatmentPlanService.getAccessibleTreatmentPlan(item.getTreatmentPlanId());
            if(!plan.getPatientId().equals(completedTreatment.getPatientId())) {
                throw new IllegalArgumentException("appointment and treatment plan patients don't match");
            }
        }
    }

}

