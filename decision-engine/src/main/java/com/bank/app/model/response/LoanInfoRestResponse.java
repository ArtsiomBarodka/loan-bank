package com.bank.app.model.response;

import com.bank.app.component.BigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

public record LoanInfoRestResponse(@JsonSerialize(using = BigDecimalSerializer.class) BigDecimal amount, Integer period) {
}
