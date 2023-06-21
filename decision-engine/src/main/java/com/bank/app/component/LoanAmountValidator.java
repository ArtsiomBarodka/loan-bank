package com.bank.app.component;

import com.bank.app.config.PropertiesConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class LoanAmountValidator implements ConstraintValidator<LoanAmountValid, BigDecimal> {
    private final PropertiesConfig propertiesConfig;

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        formatMessage(context);

        return value.compareTo(propertiesConfig.getMinCreditAmount()) >= 0 &&
                value.compareTo(propertiesConfig.getMaxCreditAmount()) <= 0;
    }

    private void formatMessage(ConstraintValidatorContext context) {
        var msg = context.getDefaultConstraintMessageTemplate();
        var formattedMsg = msg.formatted(propertiesConfig.getMinCreditAmount(), propertiesConfig.getMaxCreditAmount());
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(formattedMsg)
                .addConstraintViolation();
    }
}
