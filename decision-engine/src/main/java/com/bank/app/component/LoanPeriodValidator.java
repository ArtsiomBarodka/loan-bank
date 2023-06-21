package com.bank.app.component;

import com.bank.app.config.PropertiesConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoanPeriodValidator implements ConstraintValidator<LoanPeriodValid, Integer> {
    private final PropertiesConfig propertiesConfig;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        formatMessage(context);

        return value >= propertiesConfig.getMinCreditPeriod() &&
                value <= propertiesConfig.getMaxCreditPeriod();
    }

    private void formatMessage(ConstraintValidatorContext context) {
        var msg = context.getDefaultConstraintMessageTemplate();
        var formattedMsg = msg.formatted(propertiesConfig.getMinCreditPeriod(), propertiesConfig.getMaxCreditPeriod());
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(formattedMsg)
                .addConstraintViolation();
    }
}
