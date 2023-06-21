package com.bank.app.model.response;

import com.bank.app.model.dto.DeclineReason;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DecisionRestResponse(Long userCode,
                                   Boolean isApproved,
                                   LoanInfoRestResponse requestedLoanInfo,
                                   LoanInfoRestResponse minPeriodLoadInfo,
                                   LoanInfoRestResponse maxAmountLoadInfo,
                                   DeclineReason declineReason) {
}
