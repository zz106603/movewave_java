package com.movewave.common.model.response;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Swagger API 문서에서 공통적으로 사용되는 응답 코드들을 정의하는 커스텀 어노테이션입니다.
 * 컨트롤러 메서드에 적용하여 기본적인 HTTP 응답 상태를 문서화할 수 있습니다.
 */
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "400", description = "파라미터 오류"),
        @ApiResponse(responseCode = "401", description = "인증받지 않은 사용자"),
        @ApiResponse(responseCode = "403", description = "권한 부족"),
        @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음")
})
public @interface SwaggerCommonResponses {
}
