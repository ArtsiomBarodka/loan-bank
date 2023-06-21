package com.bank.app.model.dto;

import java.math.BigDecimal;

public record UserInfo(Long userCode, Boolean hasDept, BigDecimal creditModifier) {
    public UserInfo(Long userCode, Boolean hasDept) {
        this(userCode, hasDept, null);
    }
}
