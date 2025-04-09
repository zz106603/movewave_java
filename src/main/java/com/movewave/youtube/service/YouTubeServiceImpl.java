package com.movewave.youtube.service;

import com.movewave.common.properties.ApiKeyProperties;
import com.movewave.youtube.client.YouTubeApiClient;
import com.movewave.youtube.model.response.YouTubeResult;
import com.movewave.youtube.model.response.YouTubeSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class YouTubeServiceImpl implements YouTubeService{

    private final String youtubeVidioUrl = "https://www.youtube.com/watch?v=";

    private final ApiKeyProperties apiKeyProperties;

    private final YouTubeApiClient youTubeApiClient;

    @Override
    @Cacheable(value = "youtubeCache", key = "'youtube:' + #query.replace(' ', '_') + ':max=' + #maxResults")
    public List<YouTubeResult> searchMultiple(String query, int maxResults) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("검색어는 필수입니다.");
        }

        // YouTubeApiClient 사용
        YouTubeSearchResponse response = youTubeApiClient.search(query, maxResults, apiKeyProperties.key());

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
}
