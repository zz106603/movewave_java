package com.movewave.user.controller;

import com.movewave.common.security.CustomUserDetails;
import com.movewave.user.model.response.AccountResponse;
import com.movewave.user.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 계정 관련 API를 처리하는 컨트롤러입니다.
 * 계정 정보 조회와 로그아웃 기능을 제공합니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController implements AccountApiDoc {

    private final AccountService accountService;

    /**
     * 계정 정보를 조회합니다.
     * 현재 인증된 사용자의 계정 정보를 반환합니다.
     */
    @GetMapping("")
    @Override
    public AccountResponse getAccountInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.debug("계정 정보 조회 요청 - accountId: {}", userDetails.getAccountId());
        AccountResponse response = accountService.getAccountInfo(userDetails.getAccountId());
        log.debug("계정 정보 조회 완료 - response: {}", response);
        return response;
    }

    /**
     * 로그아웃을 처리합니다.
     * 쿠키를 삭제하여 인증 정보를 제거합니다.
     */
    @PostMapping("/logout")
    @Override
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        log.debug("로그아웃 요청");
        accountService.logout(response);
        log.debug("로그아웃 완료");
        return ResponseEntity.ok().build();
    }
}