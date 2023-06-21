package com.bank.app.model.dto;

import lombok.Data;

@Data
public class Decision {
    private Long userCode;
    private Boolean isApproved;
    private LoanInfo requestedLoanInfo;
    private LoanInfo minPeriodLoadInfo;
    private LoanInfo maxAmountLoadInfo;
    private DeclineReason declineReason;
}
