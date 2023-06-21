package com.bank.app.model.response;

public record UserClientResponse(Long userCode, Boolean hasDebt, SegmentClientResponse segmentClientResponse) {
}
