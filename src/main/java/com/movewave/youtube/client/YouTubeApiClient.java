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
    private static final String SEARCH_PATH = "/search";
    private static final String PART_PARAM = "part";
    private static final String QUERY_PARAM = "q";
    private static final String TYPE_PARAM = "type";
    private static final String MAX_RESULTS_PARAM = "maxResults";
    private static final String API_KEY_PARAM = "key";
    private static final String VIDEO_TYPE = "video";
    private static final String SNIPPET_PART = "snippet";

    public YouTubeApiClient(@Qualifier("youTubeWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public YouTubeSearchResponse search(String query, int maxResults, String apiKey) {
        String uri = UriComponentsBuilder.fromPath(SEARCH_PATH)
                .queryParam(PART_PARAM, SNIPPET_PART)
                .queryParam(QUERY_PARAM, query)
                .queryParam(TYPE_PARAM, VIDEO_TYPE)
                .queryParam(MAX_RESULTS_PARAM, maxResults)
                .queryParam(API_KEY_PARAM, apiKey)
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
