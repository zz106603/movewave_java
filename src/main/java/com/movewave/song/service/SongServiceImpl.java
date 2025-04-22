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
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final EmotionService emotionService;
    private final YouTubeService youTubeService;

    @Override
    public SongResponse analyzeAndRecommend(SongRequest request) {
        try {
            EmotionResponse emotion = emotionService.analyzeEmotion(request.text(), request.type()).get();

            // 감정 결과에 따른 검색 키워드 선택
            String searchKeyword = pickRandomSearchKeyword(emotion.keywords());

            // 유튜브 실시간 검색 (5개)
            List<YouTubeResult> youtubeResults = youTubeService.searchYouTubeVideos(searchKeyword, 5);

            // 결과를 SongWithYoutube 리스트로 변환
            List<SongWithYoutube> songs = mapToSongList(youtubeResults);

            return SongResponse.from(emotion, searchKeyword, songs);
        } catch (InterruptedException | ExecutionException e) {
            log.error("감정 분석 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("감정 분석 처리 실패", e);
        }
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
}
