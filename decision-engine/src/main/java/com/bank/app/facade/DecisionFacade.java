package com.bank.app.facade;

import com.bank.app.model.dto.Decision;
import com.bank.app.model.request.DecisionRequest;
import org.springframework.lang.NonNull;

public interface DecisionFacade {
    @NonNull Decision getDecision(@NonNull DecisionRequest decisionRequest);
}
