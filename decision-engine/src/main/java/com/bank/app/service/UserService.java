package com.bank.app.service;

import com.bank.app.model.dto.UserInfo;
import org.springframework.lang.NonNull;

public interface UserService {
    @NonNull UserInfo getUserByCode(@NonNull Long userCode);
}
