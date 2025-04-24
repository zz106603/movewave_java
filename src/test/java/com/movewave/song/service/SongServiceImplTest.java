package com.movewave.song.service;

import com.movewave.emotion.model.response.EmotionResponse;
import com.movewave.emotion.service.EmotionService;
import com.movewave.song.model.request.SongRequest;
import com.movewave.song.model.response.SongResponse;
import com.movewave.youtube.model.response.YouTubeResult;
import com.movewave.youtube.service.YouTubeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SongServiceImplTest {

    @Mock
    private EmotionService emotionService;

    @Mock
    private YouTubeService youTubeService;

    @InjectMocks
    private SongServiceImpl songService;

    private static final String TEST_TEXT = "오늘은 정말 행복한 하루였다";
    private static final String TEST_TYPE = "일기";
    private static final String TEST_PREDICTION = "기쁨";
    private static final double TEST_CONFIDENCE = 0.95;
    private static final List<String> TEST_KEYWORDS = List.of("행복", "하루");
    private static final String TEST_VIDEO_TITLE = "행복한 노래";
    private static final String TEST_THUMBNAIL_URL = "http://test.thumbnail.url";
    private static final String TEST_VIDEO_URL = "http://test.video.url";
    private static final String TEST_MUSIC_URL = "http://test.music.url";
    private static final String TEST_VIDEO_ID = "testVideoId";

    private YouTubeResult youtubeResult;

    @BeforeEach
    void setUp() {
        // 기본적인 감정 분석 응답 설정
        EmotionResponse emotionResponse = new EmotionResponse(TEST_PREDICTION, TEST_CONFIDENCE, TEST_KEYWORDS);
        given(emotionService.analyzeEmotion(any(), any()))
                .willReturn(emotionResponse);

        // 기본적인 유튜브 검색 결과 설정
        youtubeResult = new YouTubeResult(
                TEST_VIDEO_TITLE,
                TEST_THUMBNAIL_URL,
                TEST_VIDEO_URL,
                TEST_MUSIC_URL,
                TEST_VIDEO_ID
        );
    }

    @Test
    @DisplayName("정상적인 감정 분석과 유튜브 검색이 성공하는 경우")
    void getRecommendedSongs_Success() {
        // given
        SongRequest request = new SongRequest(TEST_TEXT, TEST_TYPE);

        given(youTubeService.searchYouTubeVideos(any(), anyInt()))
                .willReturn(List.of(youtubeResult));

        // when
        SongResponse response = songService.analyzeAndRecommend(request);

        // then
        assertThat(response.emotion()).isEqualTo(TEST_PREDICTION);
        assertThat(response.confidence()).isEqualTo(TEST_CONFIDENCE);
        assertThat(response.songs()).hasSize(1);
        assertThat(response.songs().get(0).title()).isEqualTo(TEST_VIDEO_TITLE);
        assertThat(response.songs().get(0).videoId()).isEqualTo(TEST_VIDEO_ID);
        assertThat(response.songs().get(0).thumbnailUrl()).isEqualTo(TEST_THUMBNAIL_URL);
        assertThat(response.songs().get(0).videoUrl()).isEqualTo(TEST_VIDEO_URL);
        assertThat(response.songs().get(0).musicUrl()).isEqualTo(TEST_MUSIC_URL);
    }

    @Test
    @DisplayName("키워드가 비어있는 경우 기본 키워드 사용")
    void getRecommendedSongs_EmptyKeywords() {
        // given
        SongRequest request = new SongRequest(TEST_TEXT, TEST_TYPE);
        EmotionResponse emotionResponse = new EmotionResponse(TEST_PREDICTION, TEST_CONFIDENCE, List.of());
        given(emotionService.analyzeEmotion(any(), any()))
                .willReturn(emotionResponse);

        // when
        SongResponse response = songService.analyzeAndRecommend(request);

        // then
        assertThat(response.keyword()).isEqualTo("감성 노래");
    }

    @Test
    @DisplayName("감정 분석이 실패하는 경우 예외 발생")
    void getRecommendedSongs_EmotionAnalysisFailure() {
        // given
        SongRequest request = new SongRequest(TEST_TEXT, TEST_TYPE);
        given(emotionService.analyzeEmotion(any(), any()))
                .willThrow(new RuntimeException("감정 분석 실패"));

        // when
        SongResponse result = songService.analyzeAndRecommend(request);

        // then
        assertThat(result.emotion()).isEqualTo("중립");
        assertThat(result.confidence()).isEqualTo(0.0);
        assertThat(result.keyword()).isEqualTo("편안한 음악");
    }

    @Test
    @DisplayName("유튜브 검색 결과가 없는 경우 빈 리스트 반환")
    void getRecommendedSongs_NoYouTubeResults() {
        // given
        SongRequest request = new SongRequest(TEST_TEXT, TEST_TYPE);
        given(youTubeService.searchYouTubeVideos(any(), anyInt()))
                .willReturn(List.of());

        // when
        SongResponse response = songService.analyzeAndRecommend(request);

        // then
        assertThat(response.songs()).isEmpty();
    }
}
