package com.bank.app.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Component
public class PropertiesConfig {
    @Value("${bank.credit.min-score}")
    private BigDecimal minCreditScore;

    @Value("${bank.credit.max-amount}")
    private BigDecimal maxCreditAmount;

    @Value("${bank.credit.min-amount}")
    private BigDecimal minCreditAmount;

    @Value("${bank.credit.min-period}")
    private Integer minCreditPeriod;

    @Value("${bank.credit.max-period}")
    private Integer maxCreditPeriod;
}
