package com.movewave.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.movewave.user.model.response.AccountResponse;

import org.springframework.http.ResponseEntity;

import com.movewave.common.model.response.SwaggerCommonResponses;
import com.movewave.common.security.CustomUserDetails;

@Tag(name = "Account", description = "사용자 관련 API")
@SecurityRequirement(name = "bearerAuth")
@SwaggerCommonResponses
public interface AccountApiDoc {

    /**
     * 계정 정보를 조회합니다.
     * @param userDetails 조회할 계정 ID
     * @return 계정 정보
     */
    @Operation(
        summary = "계정 정보 조회",
        description = "계정 ID를 통해 계정 정보를 조회합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "계정 정보를 성공적으로 조회했습니다."
            ),
            @ApiResponse(
                responseCode = "404", 
                description = "해당 계정을 찾을 수 없습니다."
            )
        }
    )
    AccountResponse getAccountInfo(CustomUserDetails userDetails);

    /**
     * 로그아웃 처리
     * @param response 응답 객체
     * @return 응답 엔티티
     */
    @Operation(summary = "로그아웃", description = "로그아웃 처리를 합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "로그아웃 처리가 성공적으로 완료되었습니다."
    )
    ResponseEntity<Void> logout(HttpServletResponse response);
}
