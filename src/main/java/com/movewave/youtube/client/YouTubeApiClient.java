package com.movewave.youtube.client;

import com.movewave.youtube.model.response.YouTubeSearchResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class YouTubeApiClient {

    private final WebClient webClient;

    public YouTubeApiClient(@Qualifier("youTubeWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public YouTubeSearchResponse search(String query, int maxResults, String apiKey) {
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
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("YouTube API 응답 오류: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("YouTube API 호출 실패", e);
        }
    }
}
