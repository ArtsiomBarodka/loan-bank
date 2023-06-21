package com.bank.app.model.request;

import com.bank.app.component.LoanAmountValid;
import com.bank.app.component.LoanPeriodValid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DecisionRequest {
    @NotNull
    @Positive
    private Long userCode;

    @NotNull
    @LoanAmountValid
    @Digits(integer = 5, fraction = 2)
    private BigDecimal loanAmount;

    @NotNull
    @LoanPeriodValid
    private Integer loanPeriod;
}
