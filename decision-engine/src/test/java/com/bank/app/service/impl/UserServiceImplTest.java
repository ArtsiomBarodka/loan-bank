package com.bank.app.service.impl;

import com.bank.app.client.UserClient;
import com.bank.app.exception.ResourceNotFoundException;
import com.bank.app.model.response.SegmentClientResponse;
import com.bank.app.model.response.UserClientResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserByCodeTest() {
        var userCode = 1L;
        var segmentClientResponse = new SegmentClientResponse(1L, BigDecimal.TEN);
        var userClientResponse = new UserClientResponse(userCode, false, segmentClientResponse);

        when(userClient.getUserByCode(anyLong())).thenReturn(userClientResponse);

        var result = userService.getUserByCode(userCode);

        assertNotNull(result);
        assertEquals(userCode, result.userCode());
        assertEquals(segmentClientResponse.creditModifier(), result.creditModifier());
        assertEquals(userClientResponse.hasDebt(), result.hasDept());
    }

    @Test
    void getUserByCodeTest_userIsNotFound() {
        var userCode = 1L;

        when(userClient.getUserByCode(anyLong())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, ()-> userService.getUserByCode(userCode));
    }

    @Test
    void getUserByCodeTest_withDeptAndNoSegment() {
        var userCode = 1L;
        var userClientResponse = new UserClientResponse(userCode, true, null);

        when(userClient.getUserByCode(anyLong())).thenReturn(userClientResponse);

        var result = userService.getUserByCode(userCode);

        assertNotNull(result);
        assertEquals(userCode, result.userCode());
        assertEquals(userClientResponse.hasDebt(), result.hasDept());
        assertTrue(result.hasDept());
        assertNull(result.creditModifier());
    }

    @Test
    void getUserByCodeTest_noDeptAndNoSegment() {
        var userCode = 1L;
        var userClientResponse = new UserClientResponse(userCode, false, null);

        when(userClient.getUserByCode(anyLong())).thenReturn(userClientResponse);

        assertThrows(ResourceNotFoundException.class, ()-> userService.getUserByCode(userCode));
    }
}
