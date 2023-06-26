package com.bank.app.component;

import com.bank.app.config.PropertiesConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class LoanPeriodValidator implements ConstraintValidator<LoanPeriodValid, String> {
    private static final Pattern DIGITS_PATTERN = Pattern.compile("-?\\d+");

    private final PropertiesConfig propertiesConfig;
    private final MessageSource messageSource;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (!DIGITS_PATTERN.matcher(value).matches()) {
            return true;
        }

        var valueLong = Long.parseLong(value);
        formatMessage(context);

        return valueLong >= propertiesConfig.getMinCreditPeriod() &&
                valueLong <= propertiesConfig.getMaxCreditPeriod();
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
