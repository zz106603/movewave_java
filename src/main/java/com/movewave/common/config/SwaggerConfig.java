package com.movewave.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/*
    http://localhost:8080/swagger-ui/index.html#/
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Movewave API",
                version = "1.0",
                description = "Movewave API Documents<br><br>" +
                        "토큰 발급: http://localhost:8080/oauth2/authorization/google"
        ),
//        security = @SecurityRequirement(name = "bearerAuth"),
        servers = {
                @Server(url = "/",
                        description = "서버 URL"),
        }
)
//@SecurityScheme(
//        name = "bearerAuth",
//        type = SecuritySchemeType.HTTP,
//        scheme = "bearer",
//        bearerFormat = "JWT"
//)

public class SwaggerConfig {
}
