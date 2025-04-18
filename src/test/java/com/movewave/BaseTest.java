package com.movewave;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

/**
 * 모든 테스트의 기본 설정을 담당하는 추상 클래스입니다.
 * 환경 변수와 시스템 프로퍼티를 설정합니다.
 */
public abstract class BaseTest {

    private static final Map<String, String> ENV_MAPPING = Map.ofEntries(
            Map.entry("google.client-id", "GOOGLE_CLIENT_ID"),
            Map.entry("google.client-secret", "GOOGLE_CLIENT_SECRET"), 
            Map.entry("jwt.secret", "JWT_SECRET"),
            Map.entry("redirect.url", "REDIRECT_URL")
    );

    @BeforeAll
    public static void setUp() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        ENV_MAPPING.forEach((systemKey, envKey) -> {
            String value = dotenv.get(envKey);
            if (value == null) {
                value = System.getenv(envKey);
            }
            if (value != null) {
                System.setProperty(systemKey, value);
            }
        });
    }
}
