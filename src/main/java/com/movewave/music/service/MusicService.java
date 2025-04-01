package com.movewave.music.service;

import com.movewave.music.domain.Song;
import com.movewave.music.model.request.MusicRequest;
import com.movewave.music.model.response.MusicResponse;

import java.util.List;

public interface MusicService {
    public List<MusicResponse> getRecommendSongs(MusicRequest request);
}
