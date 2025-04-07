package com.movewave.song.service;

import com.movewave.common.properties.ApiKeyProperties;
import com.movewave.song.model.response.YouTubeResult;
import com.movewave.song.model.youtube.YouTubeItem;
import com.movewave.song.model.youtube.YouTubeSearchResponse;
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
        YouTubeSearchResponse response = callYouTubeApi(uri);

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

    private YouTubeSearchResponse callYouTubeApi(String uri) {
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

    private YouTubeResult parseYouTubeResponse(YouTubeSearchResponse response, String query) {
        if (response == null || response.items() == null || response.items().isEmpty()) {
            throw new NoSuchElementException("YouTube 검색 결과가 없습니다.");
        }

        YouTubeItem item = response.items().get(0);
        String videoId = item.id().videoId();
        String thumbnailUrl = item.snippet().thumbnails().high().url();
        String videoUrl = youtubeVidioUrl + videoId;

        return new YouTubeResult(query, thumbnailUrl, videoUrl, videoId);
    }
}
