package com.movewave.user.model.request;

import com.movewave.user.domain.enums.AccountLoginType;
import com.movewave.user.domain.enums.AccountRole;

public record AccountCreateRequest(
        String loginId,
        Long memberId,
        AccountLoginType loginType,
        AccountRole role
) {
}