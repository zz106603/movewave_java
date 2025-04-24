package com.movewave.song.service;

import com.movewave.emotion.model.response.EmotionResponse;
import com.movewave.emotion.service.EmotionService;
import com.movewave.song.model.request.SongRequest;
import com.movewave.song.model.response.SongResponse;
import com.movewave.song.model.response.SongWithYoutube;
import com.movewave.youtube.model.response.YouTubeResult;
import com.movewave.youtube.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    /** 기본 감정 상태 */
    private static final String NEUTRAL_EMOTION = "중립";
    /** 기본 신뢰도 값 */
    private static final double DEFAULT_CONFIDENCE = 0.0;
    /** 기본 키워드 목록 */
    private static final List<String> DEFAULT_KEYWORDS = List.of("편안한 음악");

    private final EmotionService emotionService;
    private final YouTubeService youTubeService;

    @Override
    public SongResponse analyzeAndRecommend(SongRequest request) {
        EmotionResponse emotion;
        try {
            emotion = emotionService.analyzeEmotion(request.text(), request.type());
        } catch (Exception e) {
            log.warn("감정 분석 실패 → fallbackEmotion 사용: {}", e.getMessage());
            emotion = fallbackEmotion();
        }

        String searchKeyword = pickRandomSearchKeyword(emotion.keywords());
        List<YouTubeResult> youtubeResults = youTubeService.searchYouTubeVideos(searchKeyword, 5);
        List<SongWithYoutube> songs = mapToSongList(youtubeResults);

        return SongResponse.from(emotion, searchKeyword, songs);
    }

    private String pickRandomSearchKeyword(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return "감성 노래";
        }
        return keywords.get(new Random().nextInt(keywords.size()));
    }

    private List<SongWithYoutube> mapToSongList(List<YouTubeResult> youtubeResults) {
        return youtubeResults.stream()
            .map(result -> new SongWithYoutube(
                    result.videoTitle(),
                    result.thumbnailUrl(),
                    result.videoUrl(),
                    result.musicUrl(),
                    result.videoId()
            ))
            .toList();
    }

    private EmotionResponse fallbackEmotion() {
        return new EmotionResponse(NEUTRAL_EMOTION, DEFAULT_CONFIDENCE, DEFAULT_KEYWORDS);
    }
}
