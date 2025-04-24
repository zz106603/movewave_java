package com.movewave.youtube.service;

import com.movewave.common.properties.ApiKeyProperties;
import com.movewave.youtube.client.YouTubeApiClient;
import com.movewave.youtube.model.response.YouTubeResult;
import com.movewave.youtube.model.response.YouTubeSearchResponse;
import com.movewave.youtube.model.response.YouTubeItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * YouTube 검색 기능을 구현한 서비스 클래스입니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class YouTubeServiceImpl implements YouTubeService {

    private static final String YOUTUBE_VIDEO_URL = "https://www.youtube.com/watch?v=";
    private static final String YOUTUBE_MUSIC_URL = "https://music.youtube.com/watch?v=";
    private static final String YOUTUBE_PART = "snippet";
    private static final String YOUTUBE_TYPE = "video";

    private final ApiKeyProperties apiKeyProperties;
    private final YouTubeApiClient youTubeApiClient;

    /**
     * 주어진 검색어로 YouTube 동영상을 검색하고 결과를 반환합니다.
     * 검색 결과는 캐시되어 동일한 검색어에 대한 반복 요청 시 API 호출을 줄입니다.
     *
     * @param query 검색할 키워드
     * @param maxResults 검색 결과의 최대 개수
     * @return 검색된 동영상 정보 목록
     * @throws IllegalArgumentException 검색어가 null이거나 비어있는 경우
     * @throws NoSuchElementException 검색 결과가 없는 경우
     */
    @Override
    @Cacheable(value = "youtubeCache", key = "'youtube:' + #query.replace(' ', '_') + ':max=' + #maxResults")
    public List<YouTubeResult> searchYouTubeVideos(String query, int maxResults) {
        validateQuery(query);
        YouTubeSearchResponse response = youTubeApiClient.searchVideos(
            YOUTUBE_PART, query, YOUTUBE_TYPE, maxResults, apiKeyProperties.key()
        );
        validateResponse(response);
        return mapToYouTubeResults(response.items());
    }

    /**
     * 검색어의 유효성을 검사합니다.
     */
    private void validateQuery(String query) {
        if (query == null || query.isBlank()) { 
            throw new IllegalArgumentException("검색어는 필수입니다.");
        }
    }

    /**
     * 검색 응답의 유효성을 검사합니다.
     */
    private void validateResponse(YouTubeSearchResponse response) {
        if (response == null || response.items() == null || response.items().isEmpty()) {
            throw new NoSuchElementException("YouTube 검색 결과가 없습니다.");
        }
    }

    /**
     * YouTube API 응답을 YouTubeResult 목록으로 변환합니다.
     */
    private List<YouTubeResult> mapToYouTubeResults(List<YouTubeItem> items) {
        return items.stream()
                .map(this::createYouTubeResult)
                .toList();
    }

    /**
     * 단일 YouTube 항목을 YouTubeResult 객체로 변환합니다.
     */
    private YouTubeResult createYouTubeResult(YouTubeItem item) {
        String videoId = item.id().videoId();
        return new YouTubeResult(
                StringEscapeUtils.unescapeHtml4(item.snippet().title()),
                item.snippet().thumbnails().high().url(),
                YOUTUBE_VIDEO_URL + videoId,
                YOUTUBE_MUSIC_URL + videoId,
                videoId
        );
    }
}
