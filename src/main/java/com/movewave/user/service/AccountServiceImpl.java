package com.movewave.user.service;

import com.movewave.common.exception.base.ErrorMessages;
import com.movewave.common.security.constants.CookieConstants;
import com.movewave.user.domain.Account;
import com.movewave.user.repository.AccountRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.movewave.user.model.response.AccountResponse;

/**
 * 계정 관련 비즈니스 로직을 처리하는 서비스 구현체입니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    /**
     * 계정 ID로 계정 정보를 조회합니다.
     * @param accountId 조회할 계정 ID
     * @return 조회된 계정 정보
     * @throws EntityNotFoundException 계정을 찾을 수 없는 경우
     */
    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountInfo(Long accountId) {
        log.debug("계정 정보 조회 시작 - accountId: {}", accountId);
        
        Account account = accountRepository.findByIdAndIsDeletedFalse(accountId)
            .orElseThrow(() -> {
                log.error("계정을 찾을 수 없음 - accountId: {}", accountId);
                return new EntityNotFoundException(
                    ErrorMessages.NOT_FOUND_ENTITY.format(Account.class.getSimpleName(), accountId)
                );
            });
            
        log.debug("계정 정보 조회 완료 - accountId: {}", accountId);
        return AccountResponse.from(account);
    }

    /**
     * 로그아웃 처리
     */
    @Override
    @Transactional
    public void logout(HttpServletResponse response) {
        String deleteAccessTokenCookie = String.format(CookieConstants.COOKIE_DELETE_ACCESS_TOKEN);
        String deleteRefreshTokenCookie = String.format(CookieConstants.COOKIE_DELETE_REFRESH_TOKEN);

        response.addHeader("Set-Cookie", deleteAccessTokenCookie);
        response.addHeader("Set-Cookie", deleteRefreshTokenCookie);
    }
}
