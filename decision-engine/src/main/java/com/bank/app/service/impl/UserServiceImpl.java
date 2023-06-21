package com.bank.app.service.impl;

import com.bank.app.client.UserClient;
import com.bank.app.exception.ResourceNotFoundException;
import com.bank.app.model.dto.UserInfo;
import com.bank.app.model.response.UserClientResponse;
import com.bank.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserClient userClient;

    @Override
    public @NonNull UserInfo getUserByCode(@NonNull Long userCode) {
        log.info("Fetching user with (code={})", userCode);
        var userClientResponse = userClient.getUserByCode(userCode);

        log.info("User with (code={}) is fetched. User: {}", userCode, userClientResponse);
        validateUserResponse(userCode, userClientResponse);
        return toUserInfo(userClientResponse);
    }

    private void validateUserResponse(Long userCode, UserClientResponse response) {
        if (response == null) {
            log.warn("User with (code={}) is not found", userCode);
            throw new ResourceNotFoundException("User with (code=%d) is not found".formatted(userCode));
        }

        if (!response.hasDebt() && response.segmentClientResponse() == null) {
            log.error("User with (code={}) and without dept doesn't have segment info", userCode);
            throw new ResourceNotFoundException("User with (code=%d) and without dept doesn't have segment info".formatted(userCode));
        }
    }

    private UserInfo toUserInfo(UserClientResponse source) {
        return source.hasDebt()
                ? new UserInfo(source.userCode(), source.hasDebt())
                : new UserInfo(source.userCode(), source.hasDebt(), source.segmentClientResponse().creditModifier());
    }
}
