package com.dentistarchive.validator;

import com.dentistarchive.entity.patient.TreatmentPlan;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TreatmentPlanValidator {

    public void validate(TreatmentPlan plan, BigDecimal subtotal) {
        validateDiscount(plan, subtotal);
    }

    private void validateDiscount(TreatmentPlan plan, BigDecimal subtotal) {
        if (plan.getDiscountPercent() != null && plan.getDiscountAmount() != null) {
            throw new IllegalArgumentException("A treatment plan cannot have both percentage and fixed discounts.");
        }

        if (plan.getDiscountAmount() != null && plan.getDiscountAmount().compareTo(subtotal) > 0) {
            throw new IllegalArgumentException("Discount amount cannot exceed the treatment plan subtotal.");
        }
    }

}

