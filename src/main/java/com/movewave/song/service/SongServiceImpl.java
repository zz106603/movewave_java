package com.movewave.song.service;

import com.movewave.emotion.model.response.EmotionResponse;
import com.movewave.emotion.service.EmotionService;
import com.movewave.song.domain.Song;
import com.movewave.song.model.request.SongRequest;
import com.movewave.song.model.response.SongResponse;
import com.movewave.song.model.response.SongWithYoutube;
import com.movewave.song.model.response.YouTubeResult;
import com.movewave.song.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    private final EmotionService emotionService;

    private final YouTubeService youTubeService;

    private static final Map<String, List<String>> keywordMap = Map.of(
            "부정", List.of("위로되는 노래", "따뜻한 감성 음악", "지친 마음을 달래는 노래"),
            "긍정", List.of("신나는 노래", "에너지 넘치는 음악", "기분 좋아지는 댄스곡"),
            "중립", List.of("편안한 음악", "잔잔한 배경음악", "감성적인 연주곡")
    );

    @Override
    public SongResponse getRecommendSongs(SongRequest request) {
        try{
            EmotionResponse emotion = emotionService.analyzeEmotion(request.text()).get();

            // 감정 결과 → 검색 키워드
            String query = pickRandomKeyword(emotion.prediction());

            // 유튜브 실시간 검색 (5개)
            List<YouTubeResult> ytResults = youTubeService.searchMultiple(query, 5);

            // 결과를 SongWithYoutube 리스트로 변환
            List<SongWithYoutube> result = ytResults.stream()
                    .map(yt -> new SongWithYoutube(
                            yt.videoTitle(),
                            yt.thumbnailUrl(),
                            yt.videoUrl(),
                            yt.videoId()
                    ))
                    .toList();

            return SongResponse.from(emotion, result);
        }catch(InterruptedException | ExecutionException e) {
            log.error("감정 분석 실패: {}", e.getMessage());
            throw new RuntimeException("감정 분석 중 오류 발생", e);
        }
    }

    private String pickRandomKeyword(String sentiment) {
        List<String> candidates = keywordMap.getOrDefault(sentiment.toLowerCase(), List.of("감성 노래"));
        return candidates.get(new Random().nextInt(candidates.size()));
    }
}
