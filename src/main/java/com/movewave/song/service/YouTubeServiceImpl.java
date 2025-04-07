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
    @Cacheable(value = "youtubeCache", key = "'youtube:' + #query.replace(' ', '_') + ':max=' + #maxResults")
    public List<YouTubeResult> searchMultiple(String query, int maxResults) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("검색어는 필수입니다.");
        }

        String uri = UriComponentsBuilder.fromPath("/search")
                .queryParam("part", "snippet")
                .queryParam("q", query)
                .queryParam("type", "video")
                .queryParam("maxResults", maxResults)
                .queryParam("key", apiKeyProperties.key())
                .build()
                .toUriString();

        YouTubeSearchResponse response = callYouTubeApi(uri);

        if (response == null || response.items() == null || response.items().isEmpty()) {
            throw new NoSuchElementException("YouTube 검색 결과가 없습니다.");
        }

        return response.items().stream().map(item -> {
            String videoTitle = item.snippet().title();
            String videoId = item.id().videoId();
            String thumbnailUrl = item.snippet().thumbnails().high().url();
            String videoUrl = youtubeVidioUrl + videoId;
            return new YouTubeResult(videoTitle, thumbnailUrl, videoUrl, videoId);
        }).toList();
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
}
