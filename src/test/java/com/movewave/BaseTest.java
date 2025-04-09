package com.movewave;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;

import java.util.Map;

public abstract class BaseTest {

    @BeforeAll
    public static void setUp() throws InterruptedException {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        Map<String, String> mapping = Map.ofEntries(
                Map.entry("google.client-id", "GOOGLE_CLIENT_ID"),
                Map.entry("google.client-secret", "GOOGLE_CLIENT_SECRET"),
                Map.entry("jwt.secret", "JWT_SECRET"),
                Map.entry("redirect.url", "REDIRECT_URL")
        );

        mapping.forEach((systemKey, envKey) -> {
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
