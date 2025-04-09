package com.movewave.youtube.service;

import com.movewave.common.properties.ApiKeyProperties;
import com.movewave.youtube.client.YouTubeApiClient;
import com.movewave.youtube.model.response.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YouTubeServiceImplTest {

    @Mock
    private ApiKeyProperties apiKeyProperties;

    @Mock
    private YouTubeApiClient youTubeApiClient;

    @InjectMocks
    private YouTubeServiceImpl youTubeService;

    private YouTubeSearchResponse youTubeSearchResponse;

    private static final String API_KEY = "dummyKey";
    private static final int MAX_RESULT = 5;

    @BeforeEach
    void setUp() {
        YouTubeThumbnail high = new YouTubeThumbnail("https://thumbnail.com");
        YouTubeThumbnails thumbnails = new YouTubeThumbnails(high);
        YouTubeSnippet snippet = new YouTubeSnippet("Test Title", thumbnails);
        YouTubeId id = new YouTubeId("abc123");
        YouTubeItem item = new YouTubeItem(id, snippet);
        youTubeSearchResponse = new YouTubeSearchResponse(List.of(item));
    }

    @Test
    @DisplayName("유튜브 검색 및 검증 -> (성공)")
    void searchMultiple_validQuery_returnsResultList() {
        when(apiKeyProperties.key()).thenReturn(API_KEY);
        when(youTubeApiClient.search("music", MAX_RESULT, API_KEY)).thenReturn(youTubeSearchResponse);

        List<YouTubeResult> results = youTubeService.searchMultiple("music", MAX_RESULT);

        assertEquals(1, results.size());
        assertEquals("abc123", results.get(0).videoId());
        assertEquals("Test Title", results.get(0).videoTitle());
    }

    @Test
    @DisplayName("유효하지 않은 텍스트(null) 검증 -> (성공)")
    void searchMultiple_nullQuery_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> youTubeService.searchMultiple(null, MAX_RESULT));
    }

    @Test
    @DisplayName("유효하지 않은 텍스트(blank) 검증 -> (성공)")
    void searchMultiple_blankQuery_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> youTubeService.searchMultiple("   ", MAX_RESULT));
    }

    @Test
    @DisplayName("비어있는 검색 결과 -> (성공)")
    void searchMultiple_noResults_throwsNoSuchElementException() {
        when(apiKeyProperties.key()).thenReturn(API_KEY);
        when(youTubeApiClient.search("noResult", MAX_RESULT, API_KEY))
                .thenReturn(new YouTubeSearchResponse(List.of()));

        assertThrows(NoSuchElementException.class, () -> youTubeService.searchMultiple("noResult", MAX_RESULT));
    }

    @Test
    @DisplayName("존재하지 않는 검색 결과(null) -> (성공)")
    void searchMultiple_nullItems_throwsNoSuchElementException() {
        when(apiKeyProperties.key()).thenReturn(API_KEY);
        when(youTubeApiClient.search("nullItems", MAX_RESULT, API_KEY))
                .thenReturn(new YouTubeSearchResponse(null));

        assertThrows(NoSuchElementException.class, () -> youTubeService.searchMultiple("nullItems", MAX_RESULT));
    }
}