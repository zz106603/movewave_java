//package com.movewave.song.service;
//
//import com.movewave.emotion.model.response.EmotionResponse;
//import com.movewave.emotion.service.EmotionService;
//import com.movewave.song.model.request.SongRequest;
//import com.movewave.song.model.response.SongResponse;
//import com.movewave.song.model.response.SongWithYoutube;
//import com.movewave.youtube.model.response.YouTubeResult;
//import com.movewave.song.repository.SongRepository;
//import com.movewave.youtube.service.YouTubeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class SongServiceImplTest {
//
//    @Mock
//    private SongRepository songRepository;
//
//    @Mock
//    private EmotionService emotionService;
//
//    @Mock
//    private YouTubeService youTubeService;
//
//    @InjectMocks
//    private SongServiceImpl songService;
//
//    private static final String INPUT_TEXT = "오늘 기분 너무 좋아!";
//    private static final String SENTIMENT = "긍정";
//    private static final double CONFIDENCE = 0.5;
//    private static final String KEYWORD = "신나는 노래"; // keywordMap 안에 존재하는 키워드 중 하나
//
//    @BeforeEach
//    void setUp() {
//        EmotionResponse emotionResponse = new EmotionResponse(SENTIMENT, CONFIDENCE);
//        when(emotionService.analyzeEmotion(INPUT_TEXT)).thenReturn(CompletableFuture.completedFuture(emotionResponse));
//
//        YouTubeResult yt1 = new YouTubeResult("Title1", "thumb1", "url1", "vid1");
//        YouTubeResult yt2 = new YouTubeResult("Title2", "thumb2", "url2", "vid2");
//        List<YouTubeResult> ytResults = List.of(yt1, yt2);
//
//        // 긍정 → keywordMap 중 하나인 "신나는 노래" 같은 걸 기대
//        when(youTubeService.searchMultiple(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(5)))
//                .thenReturn(ytResults);
//    }
//
//    @Test
//    @DisplayName("추천 노래 정상 응답 반환 테스트")
//    void testGetRecommendSongs_success() {
//        SongRequest request = new SongRequest(INPUT_TEXT);
//
//        SongResponse response = songService.getRecommendSongs(request);
//
//        assertEquals(SENTIMENT, response.emotion());
//        assertEquals(2, response.songs().size());
//
//        SongWithYoutube song1 = response.songs().get(0);
//        assertEquals("Title1", song1.title());
//        assertEquals("thumb1", song1.thumbnailUrl());
//        assertEquals("url1", song1.videoUrl());
//    }
//
//    @Test
//    @DisplayName("감정 분석 실패 시 예외 발생 테스트")
//    void testGetRecommendSongs_emotionFails() {
//        // 감정 분석 실패 (Exception 발생)
//        when(emotionService.analyzeEmotion(INPUT_TEXT))
//                .thenThrow(new RuntimeException("감정 분석 실패"));
//
//        SongRequest request = new SongRequest(INPUT_TEXT);
//
//        assertThrows(RuntimeException.class, () -> songService.getRecommendSongs(request));
//    }
//}
