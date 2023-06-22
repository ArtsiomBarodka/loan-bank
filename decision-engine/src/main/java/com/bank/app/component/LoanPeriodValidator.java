package com.bank.app.component;

import com.bank.app.config.PropertiesConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
public class LoanPeriodValidator implements ConstraintValidator<LoanPeriodValid, Integer> {
    private final PropertiesConfig propertiesConfig;
    private final MessageSource messageSource;

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
        var messageKey = context.getDefaultConstraintMessageTemplate();
        Integer[] messageArgs = {propertiesConfig.getMinCreditPeriod(), propertiesConfig.getMaxCreditPeriod()};
        var message = messageSource.getMessage(messageKey, messageArgs, LocaleContextHolder.getLocale());

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
