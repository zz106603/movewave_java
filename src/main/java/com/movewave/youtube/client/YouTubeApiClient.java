package com.movewave.youtube.client;

import com.movewave.youtube.model.response.YouTubeSearchResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * YouTube Data API와 통신하는 클라이언트 클래스입니다.
 */
@Component
public class YouTubeApiClient {

    private final WebClient webClient;

    public YouTubeApiClient(@Qualifier("youTubeWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * YouTube 동영상을 검색합니다.
     *
     * @param query 검색어
     * @param maxResults 최대 검색 결과 수
     * @param apiKey YouTube Data API 키
     * @return 검색 결과
     * @throws IllegalStateException YouTube API가 오류 응답을 반환한 경우
     * @throws RuntimeException API 호출 중 예외가 발생한 경우
     */
    public YouTubeSearchResponse search(String query, int maxResults, String apiKey) {
        if (query == null || query.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어는 필수입니다.");
        }
        if (maxResults <= 0) {
            throw new IllegalArgumentException("최대 검색 결과 수는 1 이상이어야 합니다.");
        }
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API 키는 필수입니다.");
        }

        String uri = UriComponentsBuilder.fromPath("/search")
                .queryParam("part", "snippet")
                .queryParam("q", query)
                .queryParam("type", "video")
                .queryParam("maxResults", maxResults)
                .queryParam("key", apiKey)
                .build()
                .toUriString();

        try {
            return webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(YouTubeSearchResponse.class)
                    .block(); // 비동기 호출을 동기로 변환
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("YouTube API 응답 오류: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("YouTube API 호출 실패", e);
        }
    }
}
