package com.movewave.song.service;

import com.movewave.common.properties.ApiKeyProperties;
import com.movewave.song.model.response.YouTubeResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class YouTubeServiceImpl implements YouTubeService{

    private final String youtubeApiUrl = "https://www.googleapis.com/youtube/v3";
    private final String youtubeVidioUrl = "https://www.youtube.com/watch?v=";

    private final ApiKeyProperties apiKeyProperties;

    private final WebClient webClient = WebClient.builder()
            .baseUrl(youtubeApiUrl)
            .build();

    @Override
    @Cacheable(value = "youtubeCache", key = "'youtube:' + #title.replace(' ', '_') + ':' + #artist.replace(' ', '_')")
    public YouTubeResult search(String title, String artist) {
        // 노래 제목, 가수 검증
        validateInput(title, artist);

        // 요청 쿼리 생성
        String query = buildQuery(title, artist);

        // Youtube 요청 URI 생성
        String uri = buildYouTubeSearchUri(query);

        // Youtube 요청
        Map<String, Object> response = callYouTubeApi(uri);

        return parseYouTubeResponse(response, query);
    }

    private void validateInput(String title, String artist) {
        if (title == null || title.isBlank() || artist == null || artist.isBlank()) {
            throw new IllegalArgumentException("title과 artist는 필수입니다.");
        }
        if (apiKeyProperties.key() == null || apiKeyProperties.key().isBlank()) {
            throw new IllegalStateException("YouTube API 키가 설정되지 않았습니다.");
        }
    }

    private String buildQuery(String title, String artist) {
        return title + " " + artist;
    }

    private String buildYouTubeSearchUri(String query) {
        return UriComponentsBuilder.fromPath("/search")
                .queryParam("part", "snippet")
                .queryParam("q", query)
                .queryParam("type", "video")
                .queryParam("maxResults", 1)
                .queryParam("key", apiKeyProperties.key())
                .build()
                .toUriString();
    }

    private Map<String, Object> callYouTubeApi(String uri) {
        try {
            return webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new IllegalStateException("YouTube API 응답 오류: " + e.getStatusCode(), e);
        } catch (Exception e) {
            throw new RuntimeException("YouTube API 호출 실패", e);
        }
    }

    @SuppressWarnings("unchecked")
    private YouTubeResult parseYouTubeResponse(Map<String, Object> response, String query) {
        if (response == null || !response.containsKey("items")) {
            throw new IllegalStateException("YouTube 응답에 items 필드가 없습니다.");
        }

        List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
        if (items.isEmpty()) {
            throw new NoSuchElementException("YouTube 검색 결과가 없습니다.");
        }

        Map<String, Object> item = items.get(0);
        Map<String, Object> id = (Map<String, Object>) item.get("id");
        Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
        Map<String, Object> thumbnails = (Map<String, Object>) ((Map<String, Object>) snippet.get("thumbnails")).get("high");

        String thumbnailUrl = (String) thumbnails.get("url");
        String videoId = (String) id.get("videoId");
        String videoUrl = youtubeVidioUrl + videoId;

        return new YouTubeResult(query, thumbnailUrl, videoUrl);
    }
}
