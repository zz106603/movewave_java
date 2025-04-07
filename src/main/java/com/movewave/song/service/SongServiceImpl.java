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
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;

    private final EmotionService emotionService;

    private final YouTubeService youTubeService;

    @Override
    public SongResponse getRecommendSongs(SongRequest request) {
        try{
            EmotionResponse emotion = emotionService.analyzeEmotion(request.text()).get();
            Pageable limit = PageRequest.of(0, 6);
            List<Song> songs = songRepository.findRandomSongsByEmotion(emotion.prediction(), limit);

            // Youtube 정보
            List<SongWithYoutube> result = songs.stream()
                    .map(song -> {
                        YouTubeResult yt = youTubeService.search(song.getTitle(), song.getArtist());
                        return new SongWithYoutube(song, yt.thumbnailUrl(), yt.videoUrl(), yt.videoId());
                    })
                    .toList();

            return SongResponse.from(emotion, result);
        }catch(InterruptedException | ExecutionException e) {
            log.error("감정 분석 실패: {}", e.getMessage());
            throw new RuntimeException("감정 분석 중 오류 발생", e);
        }
    }
}
