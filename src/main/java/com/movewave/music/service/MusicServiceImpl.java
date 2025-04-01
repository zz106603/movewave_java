package com.movewave.music.service;

import com.movewave.emotion.model.response.EmotionResponse;
import com.movewave.emotion.service.EmotionService;
import com.movewave.music.domain.Song;
import com.movewave.music.model.request.MusicRequest;
import com.movewave.music.model.response.MusicResponse;
import com.movewave.music.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusicServiceImpl implements MusicService{

    private final SongRepository songRepository;

    private final EmotionService emotionService;

    public List<MusicResponse> getRecommendSongs(MusicRequest request) {
        EmotionResponse emotion = emotionService.analyzeEmotion(request.text());
        List<Song> songs = songRepository.findByEmotion(emotion.prediction());
        return MusicResponse.from(songs);
    }
}
