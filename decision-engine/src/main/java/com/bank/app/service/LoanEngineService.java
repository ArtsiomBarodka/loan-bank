package com.bank.app.service;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public interface LoanEngineService {
    @NonNull BigDecimal countCreditScore(@NonNull BigDecimal loanAmount,
                                         @NonNull Integer loanPeriod,
                                         @NonNull BigDecimal creditModifier);

    @NonNull BigDecimal countMaxLoanAmount(@NonNull Integer loanPeriod,
                                           @NonNull BigDecimal creditModifier,
                                           @NonNull BigDecimal minCreditScore);

    @NonNull Integer countMinLoanPeriod(@NonNull BigDecimal loanAmount,
                                        @NonNull BigDecimal creditModifier,
                                        @NonNull BigDecimal minCreditScore);
}
