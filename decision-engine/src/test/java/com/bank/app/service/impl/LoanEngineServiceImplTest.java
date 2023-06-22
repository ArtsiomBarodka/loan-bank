package com.bank.app.service.impl;

import com.bank.app.exception.OperationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class LoanEngineServiceImplTest {
    @InjectMocks
    private LoanEngineServiceImpl loanEngineService;

    @ParameterizedTest
    @CsvSource(value = {
            "1, 1, 1",
            "200, 10, 1.2",
            "1000, 40, 1.33",
            "2500.25, 50, 2.3"
    })
    void countCreditScoreTest(BigDecimal loanAmount, Integer loanPeriod, BigDecimal creditModifier) {
        var result = loanEngineService.countCreditScore(loanAmount, loanPeriod, creditModifier);

        assertNotNull(result);
        assertTrue(BigDecimal.ZERO.compareTo(result) < 0, "Result is positive value");
    }

    @Test
    void countCreditScoreTest_zeroLoanAmount() {
        var loanAmount = BigDecimal.ZERO;
        var loanPeriod = 10;
        var creditModifier = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countCreditScore(loanAmount, loanPeriod, creditModifier));
    }

    @Test
    void countCreditScoreTest_negativeLoanAmount() {
        var loanAmount = BigDecimal.valueOf(-1L);
        var loanPeriod = 10;
        var creditModifier = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countCreditScore(loanAmount, loanPeriod, creditModifier));
    }

    @Test
    void countCreditScoreTest_zeroLoanPeriod() {
        var loanAmount = BigDecimal.ONE;
        var loanPeriod = 0;
        var creditModifier = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countCreditScore(loanAmount, loanPeriod, creditModifier));
    }

    @Test
    void countCreditScoreTest_negativeLoanPeriod() {
        var loanAmount = BigDecimal.ONE;
        var loanPeriod = -10;
        var creditModifier = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countCreditScore(loanAmount, loanPeriod, creditModifier));
    }

    @Test
    void countCreditScoreTest_zeroCreditModifier() {
        var loanAmount = BigDecimal.ONE;
        var loanPeriod = 10;
        var creditModifier = BigDecimal.ZERO;

        assertThrows(OperationException.class, () -> loanEngineService.countCreditScore(loanAmount, loanPeriod, creditModifier));
    }

    @Test
    void countCreditScoreTest_negativeCreditModifier() {
        var loanAmount = BigDecimal.ONE;
        var loanPeriod = 10;
        var creditModifier = BigDecimal.valueOf(-1L);

        assertThrows(OperationException.class, () -> loanEngineService.countCreditScore(loanAmount, loanPeriod, creditModifier));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, 1, 1",
            "10, 10, 1.2",
            "40, 4.25, 1.33",
            "50, 0.23, 0.3"
    })
    void countMaxLoanAmountTest(Integer loanPeriod, BigDecimal creditModifier, BigDecimal minCreditScore) {
        var result = loanEngineService.countMaxLoanAmount(loanPeriod, creditModifier, minCreditScore);

        assertNotNull(result);
        assertTrue(BigDecimal.ZERO.compareTo(result) < 0, "Result is positive value");
    }

    @Test
    void countMaxLoanAmountTest_zeroLoanPeriod() {
        var loanPeriod = 0;
        var creditModifier = BigDecimal.ONE;
        var minCreditScore = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countMaxLoanAmount(loanPeriod, creditModifier, minCreditScore));
    }

    @Test
    void countMaxLoanAmountTest_negativeLoanPeriod() {
        var loanPeriod = -10;
        var creditModifier = BigDecimal.ONE;
        var minCreditScore = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countMaxLoanAmount(loanPeriod, creditModifier, minCreditScore));
    }

    @Test
    void countMaxLoanAmountTest_zeroCreditModifier() {
        var loanPeriod = 10;
        var creditModifier = BigDecimal.ZERO;
        var minCreditScore = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countMaxLoanAmount(loanPeriod, creditModifier, minCreditScore));
    }

    @Test
    void countMaxLoanAmountTest_negativeCreditModifier() {
        var loanPeriod = 10;
        var creditModifier = BigDecimal.valueOf(-1L);
        var minCreditScore = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countMaxLoanAmount(loanPeriod, creditModifier, minCreditScore));
    }

    @Test
    void countMaxLoanAmountTest_zeroMinCreditScore() {
        var loanPeriod = 10;
        var creditModifier = BigDecimal.ONE;
        var minCreditScore = BigDecimal.ZERO;

        assertThrows(OperationException.class, () -> loanEngineService.countMaxLoanAmount(loanPeriod, creditModifier, minCreditScore));
    }

    @Test
    void countMaxLoanAmountTest_negativeMinCreditScore() {
        var loanPeriod = 10;
        var creditModifier = BigDecimal.ONE;
        var minCreditScore = BigDecimal.valueOf(-1L);

        assertThrows(OperationException.class, () -> loanEngineService.countMaxLoanAmount(loanPeriod, creditModifier, minCreditScore));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1, 1, 1",
            "10.32, 10, 1.2",
            "0.15, 4.25, 1.33",
            "50, 0.23, 0.3"
    })
    void countMinLoanPeriodTest(BigDecimal loanAmount, BigDecimal creditModifier, BigDecimal minCreditScore) {
        var result = loanEngineService.countMinLoanPeriod(loanAmount, creditModifier, minCreditScore);

        assertNotNull(result);
        assertTrue(result > 0, "Result is positive value");
    }

    @Test
    void countMinLoanPeriodTest_zeroLoanAmount() {
        var loanAmount = BigDecimal.ZERO;
        var creditModifier = BigDecimal.ONE;
        var minCreditScore = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countMinLoanPeriod(loanAmount, creditModifier, minCreditScore));
    }

    @Test
    void countMinLoanPeriodTest_negativeLoanAmount() {
        var loanAmount = BigDecimal.valueOf(-1L);
        var creditModifier = BigDecimal.ONE;
        var minCreditScore = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countMinLoanPeriod(loanAmount, creditModifier, minCreditScore));
    }

    @Test
    void countMinLoanPeriodTest_zeroCreditModifier() {
        var loanAmount = BigDecimal.ONE;
        var creditModifier = BigDecimal.ZERO;
        var minCreditScore = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countMinLoanPeriod(loanAmount, creditModifier, minCreditScore));
    }

    @Test
    void countMinLoanPeriodTest_negativeCreditModifier() {
        var loanAmount = BigDecimal.ONE;
        var creditModifier = BigDecimal.valueOf(-1L);
        var minCreditScore = BigDecimal.ONE;

        assertThrows(OperationException.class, () -> loanEngineService.countMinLoanPeriod(loanAmount, creditModifier, minCreditScore));
    }

    @Test
    void countMinLoanPeriodTest_zeroMinCreditScore() {
        var loanAmount = BigDecimal.ONE;
        var creditModifier = BigDecimal.ONE;
        var minCreditScore = BigDecimal.ZERO;

        assertThrows(OperationException.class, () -> loanEngineService.countMinLoanPeriod(loanAmount, creditModifier, minCreditScore));
    }

    @Test
    void countMinLoanPeriodTest_negativeMinCreditScore() {
        var loanAmount = BigDecimal.ONE;
        var creditModifier = BigDecimal.ONE;
        var minCreditScore = BigDecimal.valueOf(-1L);

        assertThrows(OperationException.class, () -> loanEngineService.countMinLoanPeriod(loanAmount, creditModifier, minCreditScore));
    }
}
