package com.bank.app.component;

import com.bank.app.config.PropertiesConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class LoanAmountValidator implements ConstraintValidator<LoanAmountValid, BigDecimal> {
    private final PropertiesConfig propertiesConfig;
    private final MessageSource messageSource;

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
        var messageKey = context.getDefaultConstraintMessageTemplate();
        BigDecimal[] messageArgs = {propertiesConfig.getMinCreditAmount(), propertiesConfig.getMaxCreditAmount()};
        var message = messageSource.getMessage(messageKey, messageArgs, LocaleContextHolder.getLocale());

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
