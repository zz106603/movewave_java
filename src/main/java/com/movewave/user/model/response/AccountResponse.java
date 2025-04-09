package com.movewave.user.model.response;

import com.movewave.user.domain.Account;
import com.movewave.user.domain.enums.AccountRole;

public record AccountResponse(
        Long id,
        String loginId,
        AccountRole roles,
        MemberResponse memberResponse

) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getLoginId(),
                account.getRoles(),
                MemberResponse.from(account.getMember())
        );
    }
}
