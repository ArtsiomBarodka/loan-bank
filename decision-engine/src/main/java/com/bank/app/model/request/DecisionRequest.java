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
    @NotNull(message = "{validation.userCode.notNull}")
    @Positive(message = "{validation.userCode.positive}")
    private Long userCode;

    @NotNull(message = "{validation.loanAmount.notNull}")
    @LoanAmountValid
    @Digits(integer = 5, fraction = 2, message = "{validation.loanAmount.digits}")
    private BigDecimal loanAmount;

    @NotNull(message = "{validation.loanPeriod.notNull}")
    @LoanPeriodValid
    private Integer loanPeriod;
}
