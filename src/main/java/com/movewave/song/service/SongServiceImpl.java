package com.movewave.song.service;

import com.movewave.emotion.model.response.EmotionResponse;
import com.movewave.emotion.service.EmotionService;
import com.movewave.song.model.request.SongRequest;
import com.movewave.song.model.response.SongResponse;
import com.movewave.song.model.response.SongWithYoutube;
import com.movewave.youtube.model.response.YouTubeResult;
import com.movewave.song.repository.SongRepository;
import com.movewave.youtube.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final EmotionService emotionService;

    private final YouTubeService youTubeService;

    @Override
    public SongResponse getRecommendSongs(SongRequest request) {
        try{
            EmotionResponse emotion = emotionService.analyzeEmotion(request.text(), request.type()).get();

            // 감정 결과 → 검색 키워드
            String query = pickRandomKeyword(emotion.keywords());

            // 유튜브 실시간 검색 (5개)
            List<YouTubeResult> ytResults = youTubeService.searchMultiple(query, 5);

            // 결과를 SongWithYoutube 리스트로 변환
            List<SongWithYoutube> result = ytResults.stream()
                    .map(yt -> new SongWithYoutube(
                            yt.videoTitle(),
                            yt.thumbnailUrl(),
                            yt.videoUrl(),
                            yt.musicUrl(),
                            yt.videoId()
                    ))
                    .toList();

            return SongResponse.from(emotion, query, result);
        }catch(InterruptedException | ExecutionException e) {
            log.error("감정 분석 실패: {}", e.getMessage());
            throw new RuntimeException("감정 분석 중 오류 발생", e);
        }
    }

    private String pickRandomKeyword(List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) return "감성 노래";
        return keywords.get(new Random().nextInt(keywords.size()));
    }
}
