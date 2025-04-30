package com.movewave.common.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 캐시 설정을 위한 Configuration 클래스입니다.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 기본 캐시 TTL
     */
    private static final Duration DEFAULT_TTL = Duration.ofHours(1);

    /**
     * YouTube 캐시 TTL
     */
    private static final Duration YOUTUBE_TTL = Duration.ofHours(6);

    /**
     * Redis Cache Manager Bean을 생성하는 메서드입니다.
     * 
     * @param connectionFactory Redis Connection Factory
     * @return Redis Cache Manager
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(DEFAULT_TTL);

        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("youtubeCache", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(YOUTUBE_TTL));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}