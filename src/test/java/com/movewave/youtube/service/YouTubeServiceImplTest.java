package com.movewave.youtube.service;

import com.movewave.common.properties.ApiKeyProperties;
import com.movewave.youtube.client.YouTubeApiClient;
import com.movewave.youtube.model.response.YouTubeId;
import com.movewave.youtube.model.response.YouTubeItem;
import com.movewave.youtube.model.response.YouTubeResult;
import com.movewave.youtube.model.response.YouTubeSearchResponse;
import com.movewave.youtube.model.response.YouTubeSnippet;
import com.movewave.youtube.model.response.YouTubeThumbnail;
import com.movewave.youtube.model.response.YouTubeThumbnails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class YouTubeServiceImplTest {

    @Mock
    private ApiKeyProperties apiKeyProperties;

    @Mock
    private YouTubeApiClient youTubeApiClient;

    @InjectMocks
    private YouTubeServiceImpl youTubeService;

    private static final String TEST_API_KEY = "test-api-key";
    private static final String TEST_QUERY = "test query";
    private static final String TEST_VIDEO_ID = "testVideoId";
    private static final String TEST_TITLE = "Test Video Title";
    private static final String TEST_THUMBNAIL_URL = "http://test.thumbnail.url";
    private static final int TEST_MAX_RESULTS = 5;
    private static final String TEST_VIDEO_URL_PREFIX = "https://www.youtube.com/watch?v=";
    private static final String TEST_MUSIC_URL_PREFIX = "https://music.youtube.com/watch?v=";

    private static final String KEYWORD_ERROR_MESSAGE = "검색어는 필수입니다.";
    private static final String YOUTUBE_ERROR_MESSAGE = "YouTube 검색 결과가 없습니다.";

    @Test
    @DisplayName("정상적인 YouTube 검색 성공")
    void searchMultiple_Success() {
        // given
        given(apiKeyProperties.key()).willReturn(TEST_API_KEY);
        YouTubeSearchResponse mockResponse = createMockSearchResponse();
        given(youTubeApiClient.searchVideos(anyString(), anyString(), anyString(), anyInt(), anyString()))
                .willReturn(mockResponse);

        // when
        List<YouTubeResult> results = youTubeService.searchYouTubeVideos(TEST_QUERY, TEST_MAX_RESULTS);

        // then
        assertThat(results).hasSize(1);
        YouTubeResult result = results.getFirst();
        assertThat(result.videoTitle()).isEqualTo(TEST_TITLE);
        assertThat(result.thumbnailUrl()).isEqualTo(TEST_THUMBNAIL_URL);
        assertThat(result.videoUrl()).isEqualTo(TEST_VIDEO_URL_PREFIX + TEST_VIDEO_ID);
        assertThat(result.musicUrl()).isEqualTo(TEST_MUSIC_URL_PREFIX + TEST_VIDEO_ID);
        assertThat(result.videoId()).isEqualTo(TEST_VIDEO_ID);
    }

    @Test
    @DisplayName("검색어가 null인 경우 예외 발생")
    void searchMultiple_NullQuery() {
        // when & then
        assertThatThrownBy(() -> youTubeService.searchYouTubeVideos(null, TEST_MAX_RESULTS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(KEYWORD_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("검색어가 비어있는 경우 예외 발생")
    void searchMultiple_EmptyQuery() {
        // when & then
        assertThatThrownBy(() -> youTubeService.searchYouTubeVideos("", TEST_MAX_RESULTS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(KEYWORD_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("검색 결과가 없는 경우 예외 발생")
    void searchMultiple_NoResults() {
        // given
        given(apiKeyProperties.key()).willReturn(TEST_API_KEY);
        YouTubeSearchResponse emptyResponse = new YouTubeSearchResponse(List.of());
        given(youTubeApiClient.searchVideos(anyString(), anyString(), anyString(), anyInt(), anyString()))
                .willReturn(emptyResponse);

        // when & then
        assertThatThrownBy(() -> youTubeService.searchYouTubeVideos(TEST_QUERY, TEST_MAX_RESULTS))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(YOUTUBE_ERROR_MESSAGE);
    }

    @Test
    @DisplayName("API 응답이 null인 경우 예외 발생")
    void searchMultiple_NullResponse() {
        // given
        given(apiKeyProperties.key()).willReturn(TEST_API_KEY);
        given(youTubeApiClient.searchVideos(anyString(), anyString(), anyString(), anyInt(), anyString()))
                .willReturn(null);

        // when & then
        assertThatThrownBy(() -> youTubeService.searchYouTubeVideos(TEST_QUERY, TEST_MAX_RESULTS))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage(YOUTUBE_ERROR_MESSAGE);
    }

    private YouTubeSearchResponse createMockSearchResponse() {
        YouTubeThumbnail thumbnail = new YouTubeThumbnail(TEST_THUMBNAIL_URL);
        YouTubeThumbnails thumbnails = new YouTubeThumbnails(thumbnail);
        YouTubeSnippet snippet = new YouTubeSnippet(TEST_TITLE, thumbnails);
        YouTubeId id = new YouTubeId(TEST_VIDEO_ID);
        YouTubeItem item = new YouTubeItem(id, snippet);
        return new YouTubeSearchResponse(List.of(item));
    }
}