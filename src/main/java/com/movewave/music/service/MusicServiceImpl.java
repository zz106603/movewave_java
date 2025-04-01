package com.movewave.music.service;

import com.movewave.emotion.model.response.EmotionResponse;
import com.movewave.emotion.service.EmotionService;
import com.movewave.music.domain.Song;
import com.movewave.music.model.request.MusicRequest;
import com.movewave.music.model.response.MusicResponse;
import com.movewave.music.repository.SongRepository;
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
public class MusicServiceImpl implements MusicService{

    private final SongRepository songRepository;

    private final EmotionService emotionService;

    public List<MusicResponse> getRecommendSongs(MusicRequest request) {
        try{
            EmotionResponse emotion = emotionService.analyzeEmotion(request.text()).get();
            Pageable limit = PageRequest.of(0, 5);
            List<Song> songs = songRepository.findRandomSongsByEmotion(emotion.prediction(), limit);
            return MusicResponse.from(songs);
        }catch(InterruptedException | ExecutionException e) {
            log.error("감정 분석 실패: {}", e.getMessage());
            throw new RuntimeException("감정 분석 중 오류 발생", e);
        }
    }
}
