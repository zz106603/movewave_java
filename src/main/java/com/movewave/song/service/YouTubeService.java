package com.movewave.song.service;

import com.movewave.song.model.response.YouTubeResult;

import java.util.List;

public interface YouTubeService {
    List<YouTubeResult> searchMultiple(String query, int maxResults);
}
