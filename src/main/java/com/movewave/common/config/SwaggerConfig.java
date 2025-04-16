package com.movewave.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger API 문서 설정
 * - API 문서 기본 정보 설정
 * - JWT 인증 설정
 * - 서버 정보 설정
 * 
 * 접속 URL: http://localhost:8080/swagger-ui/index.html
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Movewave API",
                version = "1.0",
                description = """
                        Movewave API Documents
                        
                        Google OAuth2 로그인 및 토큰 발급: 
                        http://localhost:8080/oauth2/authorization/google
                        """
        ),
        security = @SecurityRequirement(name = "bearerAuth"),
        servers = {
                @Server(
                        url = "/",
                        description = "Default Server"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
