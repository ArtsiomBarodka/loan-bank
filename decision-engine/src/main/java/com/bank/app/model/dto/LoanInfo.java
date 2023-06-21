package com.bank.app.model.dto;

import java.math.BigDecimal;

public record LoanInfo(BigDecimal amount, Integer period) {
}
