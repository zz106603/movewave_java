package com.movewave.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient 설정 클래스
 * - Flask 서버와 YouTube API 통신을 위한 WebClient Bean 설정
 */
@Configuration
public class WebClientConfig {

    @Value("${flask.server.url:http://localhost:5000}")
    private String flaskServerUrl;

    @Value("${youtube.api.base-url:https://www.googleapis.com/youtube/v3}")
    private String youtubeApiBaseUrl;

    /**
     * Flask 서버 통신용 WebClient Bean
     */
    @Bean
    @Qualifier("flaskWebClient")
    public WebClient flaskWebClient(WebClient.Builder builder) {
        return createWebClient(builder, flaskServerUrl);
    }

    /**
     * YouTube API 통신용 WebClient Bean
     */
    @Bean
    @Qualifier("youTubeWebClient")
    public WebClient youTubeWebClient(WebClient.Builder builder) {
        return createWebClient(builder, youtubeApiBaseUrl);
    }

    /**
     * WebClient 생성 공통 메서드
     */
    private WebClient createWebClient(WebClient.Builder builder, String baseUrl) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
