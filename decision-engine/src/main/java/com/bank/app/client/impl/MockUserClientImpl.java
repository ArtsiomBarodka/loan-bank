package com.bank.app.client.impl;

import com.bank.app.client.UserClient;
import com.bank.app.model.response.SegmentClientResponse;
import com.bank.app.model.response.UserClientResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class MockUserClientImpl implements UserClient {
    private static final Map<Long, UserClientResponse> USER_DATA;

    static {
        USER_DATA = new HashMap<>();

        USER_DATA.put(1L, new UserClientResponse(1L, true, null));
        USER_DATA.put(2L, new UserClientResponse(2L, false, new SegmentClientResponse(1L, BigDecimal.valueOf(100L))));
        USER_DATA.put(3L, new UserClientResponse(3L, false, new SegmentClientResponse(2L, BigDecimal.valueOf(300L))));
        USER_DATA.put(4L, new UserClientResponse(4L, false, new SegmentClientResponse(3L, BigDecimal.valueOf(1000L))));
    }

    @Override
    public @Nullable UserClientResponse getUserByCode(@NonNull Long userCode) {
        return USER_DATA.get(userCode);
    }
}
