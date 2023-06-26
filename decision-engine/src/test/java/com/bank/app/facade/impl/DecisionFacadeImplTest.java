package com.bank.app.facade.impl;

import com.bank.app.config.PropertiesConfig;
import com.bank.app.model.dto.DeclineReason;
import com.bank.app.model.dto.UserInfo;
import com.bank.app.model.request.DecisionRequest;
import com.bank.app.service.LoanEngineService;
import com.bank.app.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DecisionFacadeImplTest {
    @Mock
    private UserService userService;
    @Mock
    private LoanEngineService loanEngineService;
    @Mock
    private PropertiesConfig propertiesConfig;

    @InjectMocks
    private DecisionFacadeImpl decisionFacade;

    private static DecisionRequest DECISION_REQUEST;

    @BeforeAll
    static void setup() {
        DECISION_REQUEST = new DecisionRequest();

        DECISION_REQUEST.setLoanAmount(BigDecimal.ONE);
        DECISION_REQUEST.setUserCode("1");
        DECISION_REQUEST.setLoanPeriod("10");
    }

    @Test
    void getDecisionTest() {
        var userInfo = new UserInfo(DECISION_REQUEST.getUserCode(), false, BigDecimal.ONE);
        var creditScore = BigDecimal.TEN;
        var minCreditScore = BigDecimal.ONE;
        var countedMinLoanPeriod = 5;
        var maxCreditPeriod = 40;
        var minCreditPeriod = 1;
        var countedMaxLoanAmount = BigDecimal.TEN;
        var minCreditAmount = BigDecimal.ONE;
        var maxCreditAmount = BigDecimal.valueOf(100L);

        when(userService.getUserByCode(anyLong())).thenReturn(userInfo);
        when(loanEngineService.countCreditScore(any(), any(), any())).thenReturn(creditScore);
        when(propertiesConfig.getMinCreditScore()).thenReturn(minCreditScore);
        when(loanEngineService.countMinLoanPeriod(any(), any(), any())).thenReturn(countedMinLoanPeriod);
        when(propertiesConfig.getMaxCreditPeriod()).thenReturn(maxCreditPeriod);
        when(propertiesConfig.getMinCreditPeriod()).thenReturn(minCreditPeriod);
        when(loanEngineService.countMaxLoanAmount(any(), any(), any())).thenReturn(countedMaxLoanAmount);
        when(propertiesConfig.getMinCreditAmount()).thenReturn(minCreditAmount);
        when(propertiesConfig.getMaxCreditAmount()).thenReturn(maxCreditAmount);

        var result = decisionFacade.getDecision(DECISION_REQUEST);

        assertNotNull(result);
        assertEquals(DECISION_REQUEST.getUserCode(), result.getUserCode());
        assertTrue(result.getIsApproved());
        assertNull(result.getDeclineReason());

        var requestedLoanInfoResult = result.getRequestedLoanInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), requestedLoanInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), requestedLoanInfoResult.period());

        var maxAmountLoadInfoResult = result.getMaxAmountLoadInfo();

        assertEquals(countedMaxLoanAmount, maxAmountLoadInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), maxAmountLoadInfoResult.period());

        var minPeriodLoadInfoResult = result.getMinPeriodLoadInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), minPeriodLoadInfoResult.amount());
        assertEquals(countedMinLoanPeriod, minPeriodLoadInfoResult.period());
    }

    @Test
    void getDecisionTest_withDept() {
        var userInfo = new UserInfo(DECISION_REQUEST.getUserCode(), true, BigDecimal.ONE);

        when(userService.getUserByCode(anyLong())).thenReturn(userInfo);

        var result = decisionFacade.getDecision(DECISION_REQUEST);

        assertNotNull(result);
        assertEquals(DECISION_REQUEST.getUserCode(), result.getUserCode());
        assertFalse(result.getIsApproved());
        assertNotNull(result.getDeclineReason());
        assertEquals(DeclineReason.DEPT, result.getDeclineReason());
        assertNull(result.getMaxAmountLoadInfo());
        assertNull(result.getMinPeriodLoadInfo());

        var requestedLoanInfoResult = result.getRequestedLoanInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), requestedLoanInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), requestedLoanInfoResult.period());
    }

    @Test
    void getDecisionTest_countedMinMoreThanMaxPossiblePeriod() {
        var userInfo = new UserInfo(DECISION_REQUEST.getUserCode(), false, BigDecimal.ONE);
        var creditScore = BigDecimal.TEN;
        var minCreditScore = BigDecimal.ONE;
        var countedMinLoanPeriod = 5;
        var maxCreditPeriod = 4;
        var countedMaxLoanAmount = BigDecimal.TEN;
        var minCreditAmount = BigDecimal.ONE;
        var maxCreditAmount = BigDecimal.valueOf(100L);

        when(userService.getUserByCode(anyLong())).thenReturn(userInfo);
        when(loanEngineService.countCreditScore(any(), any(), any())).thenReturn(creditScore);
        when(propertiesConfig.getMinCreditScore()).thenReturn(minCreditScore);
        when(loanEngineService.countMinLoanPeriod(any(), any(), any())).thenReturn(countedMinLoanPeriod);
        when(propertiesConfig.getMaxCreditPeriod()).thenReturn(maxCreditPeriod);
        when(loanEngineService.countMaxLoanAmount(any(), any(), any())).thenReturn(countedMaxLoanAmount);
        when(propertiesConfig.getMinCreditAmount()).thenReturn(minCreditAmount);
        when(propertiesConfig.getMaxCreditAmount()).thenReturn(maxCreditAmount);

        var result = decisionFacade.getDecision(DECISION_REQUEST);

        assertNotNull(result);
        assertEquals(DECISION_REQUEST.getUserCode(), result.getUserCode());
        assertTrue(result.getIsApproved());
        assertNull(result.getDeclineReason());
        assertNull(result.getMinPeriodLoadInfo());

        var requestedLoanInfoResult = result.getRequestedLoanInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), requestedLoanInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), requestedLoanInfoResult.period());

        var maxAmountLoadInfoResult = result.getMaxAmountLoadInfo();

        assertEquals(countedMaxLoanAmount, maxAmountLoadInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), maxAmountLoadInfoResult.period());
    }

    @Test
    void getDecisionTest_countedMinLessThanMinPossiblePeriod() {
        var userInfo = new UserInfo(DECISION_REQUEST.getUserCode(), false, BigDecimal.ONE);
        var creditScore = BigDecimal.TEN;
        var minCreditScore = BigDecimal.ONE;
        var countedMinLoanPeriod = 5;
        var maxCreditPeriod = 40;
        var minCreditPeriod = 10;
        var countedMaxLoanAmount = BigDecimal.TEN;
        var minCreditAmount = BigDecimal.ONE;
        var maxCreditAmount = BigDecimal.valueOf(100L);

        when(userService.getUserByCode(anyLong())).thenReturn(userInfo);
        when(loanEngineService.countCreditScore(any(), any(), any())).thenReturn(creditScore);
        when(propertiesConfig.getMinCreditScore()).thenReturn(minCreditScore);
        when(loanEngineService.countMinLoanPeriod(any(), any(), any())).thenReturn(countedMinLoanPeriod);
        when(propertiesConfig.getMaxCreditPeriod()).thenReturn(maxCreditPeriod);
        when(propertiesConfig.getMinCreditPeriod()).thenReturn(minCreditPeriod);
        when(loanEngineService.countMaxLoanAmount(any(), any(), any())).thenReturn(countedMaxLoanAmount);
        when(propertiesConfig.getMinCreditAmount()).thenReturn(minCreditAmount);
        when(propertiesConfig.getMaxCreditAmount()).thenReturn(maxCreditAmount);

        var result = decisionFacade.getDecision(DECISION_REQUEST);

        assertNotNull(result);
        assertEquals(DECISION_REQUEST.getUserCode(), result.getUserCode());
        assertTrue(result.getIsApproved());
        assertNull(result.getDeclineReason());

        var requestedLoanInfoResult = result.getRequestedLoanInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), requestedLoanInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), requestedLoanInfoResult.period());

        var maxAmountLoadInfoResult = result.getMaxAmountLoadInfo();

        assertEquals(countedMaxLoanAmount, maxAmountLoadInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), maxAmountLoadInfoResult.period());

        var minPeriodLoadInfoResult = result.getMinPeriodLoadInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), minPeriodLoadInfoResult.amount());
        assertEquals(minCreditPeriod, minPeriodLoadInfoResult.period());
    }

    @Test
    void getDecisionTest_countedMaxLessThanMinPossibleAmount() {
        var userInfo = new UserInfo(DECISION_REQUEST.getUserCode(), false, BigDecimal.ONE);
        var creditScore = BigDecimal.TEN;
        var minCreditScore = BigDecimal.ONE;
        var countedMinLoanPeriod = 5;
        var maxCreditPeriod = 40;
        var minCreditPeriod = 10;
        var countedMaxLoanAmount = BigDecimal.ONE;
        var minCreditAmount = BigDecimal.TEN;

        when(userService.getUserByCode(anyLong())).thenReturn(userInfo);
        when(loanEngineService.countCreditScore(any(), any(), any())).thenReturn(creditScore);
        when(propertiesConfig.getMinCreditScore()).thenReturn(minCreditScore);
        when(loanEngineService.countMinLoanPeriod(any(), any(), any())).thenReturn(countedMinLoanPeriod);
        when(propertiesConfig.getMaxCreditPeriod()).thenReturn(maxCreditPeriod);
        when(propertiesConfig.getMinCreditPeriod()).thenReturn(minCreditPeriod);
        when(loanEngineService.countMaxLoanAmount(any(), any(), any())).thenReturn(countedMaxLoanAmount);
        when(propertiesConfig.getMinCreditAmount()).thenReturn(minCreditAmount);

        var result = decisionFacade.getDecision(DECISION_REQUEST);

        assertNotNull(result);
        assertEquals(DECISION_REQUEST.getUserCode(), result.getUserCode());
        assertTrue(result.getIsApproved());
        assertNull(result.getDeclineReason());
        assertNull(result.getMaxAmountLoadInfo());

        var requestedLoanInfoResult = result.getRequestedLoanInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), requestedLoanInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), requestedLoanInfoResult.period());

        var minPeriodLoadInfoResult = result.getMinPeriodLoadInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), minPeriodLoadInfoResult.amount());
        assertEquals(minCreditPeriod, minPeriodLoadInfoResult.period());
    }

    @Test
    void getDecisionTest_countedMaxMoreThanMaxPossibleAmount() {
        var userInfo = new UserInfo(DECISION_REQUEST.getUserCode(), false, BigDecimal.ONE);
        var creditScore = BigDecimal.TEN;
        var minCreditScore = BigDecimal.ONE;
        var countedMinLoanPeriod = 5;
        var maxCreditPeriod = 40;
        var minCreditPeriod = 1;
        var countedMaxLoanAmount = BigDecimal.valueOf(100L);
        var minCreditAmount = BigDecimal.ONE;
        var maxCreditAmount = BigDecimal.TEN;

        when(userService.getUserByCode(anyLong())).thenReturn(userInfo);
        when(loanEngineService.countCreditScore(any(), any(), any())).thenReturn(creditScore);
        when(propertiesConfig.getMinCreditScore()).thenReturn(minCreditScore);
        when(loanEngineService.countMinLoanPeriod(any(), any(), any())).thenReturn(countedMinLoanPeriod);
        when(propertiesConfig.getMaxCreditPeriod()).thenReturn(maxCreditPeriod);
        when(propertiesConfig.getMinCreditPeriod()).thenReturn(minCreditPeriod);
        when(loanEngineService.countMaxLoanAmount(any(), any(), any())).thenReturn(countedMaxLoanAmount);
        when(propertiesConfig.getMinCreditAmount()).thenReturn(minCreditAmount);
        when(propertiesConfig.getMaxCreditAmount()).thenReturn(maxCreditAmount);

        var result = decisionFacade.getDecision(DECISION_REQUEST);

        assertNotNull(result);
        assertEquals(DECISION_REQUEST.getUserCode(), result.getUserCode());
        assertTrue(result.getIsApproved());
        assertNull(result.getDeclineReason());

        var requestedLoanInfoResult = result.getRequestedLoanInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), requestedLoanInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), requestedLoanInfoResult.period());

        var maxAmountLoadInfoResult = result.getMaxAmountLoadInfo();

        assertEquals(maxCreditAmount, maxAmountLoadInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), maxAmountLoadInfoResult.period());

        var minPeriodLoadInfoResult = result.getMinPeriodLoadInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), minPeriodLoadInfoResult.amount());
        assertEquals(countedMinLoanPeriod, minPeriodLoadInfoResult.period());
    }

    @Test
    void getDecisionTest_isNotApproved() {
        var userInfo = new UserInfo(DECISION_REQUEST.getUserCode(), false, BigDecimal.ONE);
        var creditScore = BigDecimal.ONE;
        var minCreditScore = BigDecimal.TEN;
        var countedMinLoanPeriod = 5;
        var maxCreditPeriod = 40;
        var minCreditPeriod = 1;
        var countedMaxLoanAmount = BigDecimal.TEN;
        var minCreditAmount = BigDecimal.ONE;
        var maxCreditAmount = BigDecimal.valueOf(100L);

        when(userService.getUserByCode(anyLong())).thenReturn(userInfo);
        when(loanEngineService.countCreditScore(any(), any(), any())).thenReturn(creditScore);
        when(propertiesConfig.getMinCreditScore()).thenReturn(minCreditScore);
        when(loanEngineService.countMinLoanPeriod(any(), any(), any())).thenReturn(countedMinLoanPeriod);
        when(propertiesConfig.getMaxCreditPeriod()).thenReturn(maxCreditPeriod);
        when(propertiesConfig.getMinCreditPeriod()).thenReturn(minCreditPeriod);
        when(loanEngineService.countMaxLoanAmount(any(), any(), any())).thenReturn(countedMaxLoanAmount);
        when(propertiesConfig.getMinCreditAmount()).thenReturn(minCreditAmount);
        when(propertiesConfig.getMaxCreditAmount()).thenReturn(maxCreditAmount);

        var result = decisionFacade.getDecision(DECISION_REQUEST);

        assertNotNull(result);
        assertEquals(DECISION_REQUEST.getUserCode(), result.getUserCode());
        assertFalse(result.getIsApproved());
        assertNotNull(result.getDeclineReason());
        assertEquals(DeclineReason.LOAN_PARAMETERS, result.getDeclineReason());

        var requestedLoanInfoResult = result.getRequestedLoanInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), requestedLoanInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), requestedLoanInfoResult.period());

        var maxAmountLoadInfoResult = result.getMaxAmountLoadInfo();

        assertEquals(countedMaxLoanAmount, maxAmountLoadInfoResult.amount());
        assertEquals(DECISION_REQUEST.getLoanPeriod(), maxAmountLoadInfoResult.period());

        var minPeriodLoadInfoResult = result.getMinPeriodLoadInfo();

        assertEquals(DECISION_REQUEST.getLoanAmount(), minPeriodLoadInfoResult.amount());
        assertEquals(countedMinLoanPeriod, minPeriodLoadInfoResult.period());
    }
}
