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
    @Digits(integer = 10, fraction = 0, message = "{validation.userCode.digits}")
    @Positive(message = "{validation.userCode.positive}")
    private String userCode;

    @NotNull(message = "{validation.loanAmount.notNull}")
    @Digits(integer = 5, fraction = 2, message = "{validation.loanAmount.digits}")
    @LoanAmountValid
    private BigDecimal loanAmount;

    @NotNull(message = "{validation.loanPeriod.notNull}")
    @Digits(integer = 2, fraction = 0, message = "{validation.loanPeriod.digits}")
    @LoanPeriodValid
    private String loanPeriod;

    public Long getUserCode() {
        if (this.userCode == null) {
            return null;
        }
        return Long.parseLong(this.userCode);
    }

    public Integer getLoanPeriod() {
        if (this.loanPeriod == null) {
            return null;
        }
        return Integer.parseInt(this.loanPeriod);
    }
}
