package com.movewave.user.service;

import com.movewave.user.model.response.AccountResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 계정 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface AccountService {

    /**
     * 계정 ID로 계정 정보를 조회합니다.
     * @param accountId 조회할 계정 ID
     * @return 조회된 계정 정보 (AccountResponse)
     * @throws EntityNotFoundException 계정을 찾을 수 없는 경우
     */
    AccountResponse getAccountInfo(Long accountId);

    /**
     * 로그아웃 처리
     */
    void logout(HttpServletResponse response);
}
