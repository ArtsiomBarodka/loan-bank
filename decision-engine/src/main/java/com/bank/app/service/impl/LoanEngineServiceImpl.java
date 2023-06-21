package com.bank.app.service.impl;

import com.bank.app.exception.OperationException;
import com.bank.app.service.LoanEngineService;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Log4j2
@Service
public class LoanEngineServiceImpl implements LoanEngineService {
    @Override
    public @NonNull BigDecimal countCreditScore(@NonNull BigDecimal loanAmount,
                                                @NonNull Integer loanPeriod,
                                                @NonNull BigDecimal creditModifier) {
        validateLoanAmount(loanAmount);
        validateLoanPeriod(loanPeriod);
        validateCreditModifier(creditModifier);

        var creditScore = (BigDecimal.valueOf(loanPeriod).multiply(creditModifier)).divide(loanAmount, 2, RoundingMode.HALF_UP);

        log.info("Counted credit score = {} for loan amount = {}, loan period = {}, credit modifier = {}",
                creditScore, loanAmount, loanPeriod, creditModifier);

        return creditScore;
    }

    @Override
    public @NonNull BigDecimal countMaxLoanAmount(@NonNull Integer loanPeriod,
                                                  @NonNull BigDecimal creditModifier,
                                                  @NonNull BigDecimal minCreditScore) {
        validateLoanPeriod(loanPeriod);
        validateCreditModifier(creditModifier);
        validateCreditScore(minCreditScore);

        var maxLoanAmount = (BigDecimal.valueOf(loanPeriod).multiply(creditModifier)).divide(minCreditScore, 2, RoundingMode.HALF_UP);

        log.info("Counted max loan amount = {} for loan period = {}, credit modifier = {},  min credit score = {}",
                maxLoanAmount, loanPeriod, creditModifier, minCreditScore);

        return maxLoanAmount;
    }

    @Override
    public @NonNull Integer countMinLoanPeriod(@NonNull BigDecimal loanAmount,
                                               @NonNull BigDecimal creditModifier,
                                               @NonNull BigDecimal minCreditScore) {
        validateLoanAmount(loanAmount);
        validateCreditModifier(creditModifier);
        validateCreditScore(minCreditScore);

        var minLoanPeriod = (minCreditScore.multiply(loanAmount)).divide(creditModifier, RoundingMode.UP);

        log.info("Counted min loan period = {} for loan amount = {}, credit modifier = {},  min credit score = {}",
                minLoanPeriod, loanAmount, creditModifier, minCreditScore);

        return minLoanPeriod.intValue();
    }

    private void validateLoanAmount(BigDecimal loanAmount) {
        if (loanAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Loan amount = {}. It can't be equal or less than 0.", loanAmount);
            throw new OperationException("Loan amount = %s. It can't be equal or less than 0.".formatted(loanAmount));
        }
    }

    private void validateLoanPeriod(Integer loanPeriod) {
        if (loanPeriod <= 0) {
            log.error("Loan period = {}. It can't be equal or less than 0.", loanPeriod);
            throw new OperationException("Loan period = %d. It can't be equal or less than 0.".formatted(loanPeriod));
        }
    }

    private void validateCreditModifier(BigDecimal creditModifier) {
        if (creditModifier.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Credit Modifier = {}. It can't be equal or less than 0.", creditModifier);
            throw new OperationException("Credit Modifier = %s. It can't be equal or less than 0.".formatted(creditModifier));
        }
    }

    private void validateCreditScore(BigDecimal creditScore) {
        if (creditScore.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Credit Score = {}. It can't be equal or less than 0.", creditScore);
            throw new OperationException("Credit Score = %s. It can't be equal or less than 0.".formatted(creditScore));
        }
    }
}
