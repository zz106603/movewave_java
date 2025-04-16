package com.movewave.user.model.response;

import com.movewave.user.domain.Account;
import com.movewave.user.domain.enums.AccountRole;

/**
 * 계정 정보를 반환하기 위한 응답 객체입니다.
 */
public record AccountResponse(
        Long id,                    // 계정 ID
        String loginId,             // 로그인 ID
        AccountRole roles,          // 계정 권한
        MemberResponse member       // 회원 정보
) {
    /**
     * Account 엔티티로부터 AccountResponse 객체를 생성합니다.
     * @param account 계정 엔티티
     * @return AccountResponse 객체
     */
    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getLoginId(), 
                account.getRole(),
                MemberResponse.from(account.getMember())
        );
    }
}
