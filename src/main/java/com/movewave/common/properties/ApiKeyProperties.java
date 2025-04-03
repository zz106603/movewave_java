package com.movewave.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
public record ApiKeyProperties(
        String key
) {
}