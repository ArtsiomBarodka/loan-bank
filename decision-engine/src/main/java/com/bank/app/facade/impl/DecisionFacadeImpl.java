package com.bank.app.facade.impl;

import com.bank.app.config.PropertiesConfig;
import com.bank.app.facade.DecisionFacade;
import com.bank.app.model.dto.Decision;
import com.bank.app.model.dto.DeclineReason;
import com.bank.app.model.dto.LoanInfo;
import com.bank.app.model.request.DecisionRequest;
import com.bank.app.service.LoanEngineService;
import com.bank.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DecisionFacadeImpl implements DecisionFacade {
    private final UserService userService;
    private final LoanEngineService loanEngineService;
    private final PropertiesConfig propertiesConfig;

    @Override
    public @NonNull Decision getDecision(@NonNull DecisionRequest decisionRequest) {
        var userInfo = userService.getUserByCode(decisionRequest.getUserCode());

        var decision = new Decision();
        decision.setUserCode(userInfo.userCode());
        decision.setRequestedLoanInfo(new LoanInfo(decisionRequest.getLoanAmount(), decisionRequest.getLoanPeriod()));

        if (userInfo.hasDept()) {
            decision.setIsApproved(false);
            decision.setDeclineReason(DeclineReason.DEPT);
        } else {
            var creditScore = loanEngineService.countCreditScore(decisionRequest.getLoanAmount(),
                    decisionRequest.getLoanPeriod(),
                    userInfo.creditModifier());

            var minPeriodLoadInfo = getMinPeriodLoadInfo(decisionRequest.getLoanAmount(), userInfo.creditModifier());
            var maxAmountLoadInfo = getMaxAmountLoadInfo(decisionRequest.getLoanPeriod(), userInfo.creditModifier());

            decision.setMaxAmountLoadInfo(maxAmountLoadInfo);
            decision.setMinPeriodLoadInfo(minPeriodLoadInfo);

            boolean isCreditApproved = (creditScore.compareTo(propertiesConfig.getMinCreditScore()) >= 0);
            decision.setIsApproved(isCreditApproved);

            if (!isCreditApproved) {
                decision.setDeclineReason(DeclineReason.LOAN_PARAMETERS);
            }
        }

        return decision;
    }

    private LoanInfo getMinPeriodLoadInfo(BigDecimal loanAmount, BigDecimal creditModifier) {
        LoanInfo loanInfo = null;

        var countedMinPeriodForExactAmount = loanEngineService.countMinLoanPeriod(loanAmount,
                creditModifier,
                propertiesConfig.getMinCreditScore());

        if (countedMinPeriodForExactAmount <= propertiesConfig.getMaxCreditPeriod()) {
            var realMinPeriodForExactAmount = countedMinPeriodForExactAmount < propertiesConfig.getMinCreditPeriod()
                    ? propertiesConfig.getMinCreditPeriod()
                    : countedMinPeriodForExactAmount;

            loanInfo = new LoanInfo(loanAmount, realMinPeriodForExactAmount);
        }

        return loanInfo;
    }

    private LoanInfo getMaxAmountLoadInfo(Integer loanPeriod, BigDecimal creditModifier) {
        LoanInfo loanInfo = null;

        var countedMaxAmountForExactPeriod = loanEngineService.countMaxLoanAmount(loanPeriod,
                creditModifier,
                propertiesConfig.getMinCreditScore());

        if (countedMaxAmountForExactPeriod.compareTo(propertiesConfig.getMinCreditAmount()) >= 0) {
            var realMaxAmountForExactPeriod = countedMaxAmountForExactPeriod.compareTo(propertiesConfig.getMaxCreditAmount()) > 0
                    ? propertiesConfig.getMaxCreditAmount()
                    : countedMaxAmountForExactPeriod;

            loanInfo = new LoanInfo(realMaxAmountForExactPeriod, loanPeriod);
        }

        return loanInfo;
    }
}
