package com.bank.app.rest;

import com.bank.app.component.LoanAmountValidator;
import com.bank.app.component.LoanPeriodValidator;
import com.bank.app.config.MessageSourceConfig;
import com.bank.app.config.PropertiesConfig;
import com.bank.app.facade.DecisionFacade;
import com.bank.app.model.dto.Decision;
import com.bank.app.model.request.DecisionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {DecisionController.class, MessageSourceConfig.class,
        LoanAmountValidator.class, LoanPeriodValidator.class, PropertiesConfig.class})
@AutoConfigureWebMvc
@AutoConfigureMockMvc
public class DecisionControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DecisionFacade decisionFacade;
    @MockBean
    private LoanAmountValidator loanAmountValidator;
    @MockBean
    private LoanPeriodValidator loanPeriodValidator;

    @Test
    void getDecisionTest() throws Exception {
        var decisionRequest = new DecisionRequest();
        decisionRequest.setUserCode(1L);
        decisionRequest.setLoanPeriod(20);
        decisionRequest.setLoanAmount(BigDecimal.valueOf(5000L));

        var decision = new Decision();

        when(loanAmountValidator.isValid(any(), any())).thenReturn(true);
        when(loanPeriodValidator.isValid(any(), any())).thenReturn(true);
        when(decisionFacade.getDecision(any())).thenReturn(decision);

        mvc.perform(post("/api/v1/bank/decisions")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(decisionRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
