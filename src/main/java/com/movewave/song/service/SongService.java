package com.movewave.song.service;

import com.movewave.song.model.request.SongRequest;
import com.movewave.song.model.response.SongResponse;

import java.util.List;

public interface SongService {
    public SongResponse getRecommendSongs(SongRequest request);
}
