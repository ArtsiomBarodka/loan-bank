package com.bank.app.client;

import com.bank.app.model.response.UserClientResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface UserClient {
    @Nullable
    UserClientResponse getUserByCode(@NonNull Long userCode);
}
