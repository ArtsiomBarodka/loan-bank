package com.bank.app.rest;

import com.bank.app.facade.DecisionFacade;
import com.bank.app.model.dto.Decision;
import com.bank.app.model.dto.LoanInfo;
import com.bank.app.model.request.DecisionRequest;
import com.bank.app.model.response.DecisionRestResponse;
import com.bank.app.model.response.LoanInfoRestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/v1/bank/decisions")
@RequiredArgsConstructor
public class DecisionController {
    private final DecisionFacade decisionFacade;

    @PostMapping
    public ResponseEntity<DecisionRestResponse> getDecision(@Valid @RequestBody DecisionRequest decisionRequest) {
        log.info("Getting decision for decision request. Decision Request: {}", decisionRequest);
        var decision = decisionFacade.getDecision(decisionRequest);

        var decisionResponse = toDecisionResponse(decision);

        log.info("Decision response is received for decision request. Decision  Response:{}. Decision Request: {}",
                decisionResponse, decisionRequest);

        return ResponseEntity.ok(decisionResponse);
    }

    private DecisionRestResponse toDecisionResponse(Decision source) {
        return new DecisionRestResponse(source.getUserCode(),
                source.getIsApproved(),
                toLoanInfoResponse(source.getRequestedLoanInfo()),
                toLoanInfoResponse(source.getMinPeriodLoadInfo()),
                toLoanInfoResponse(source.getMaxAmountLoadInfo()),
                source.getDeclineReason());
    }

    private LoanInfoRestResponse toLoanInfoResponse(LoanInfo source) {
        LoanInfoRestResponse loanInfoRestResponse = null;
        if (source != null) {
            loanInfoRestResponse = new LoanInfoRestResponse(source.amount(), source.period());
        }
        return loanInfoRestResponse;
    }
}
