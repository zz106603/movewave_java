package com.movewave.youtube.service;

import com.movewave.youtube.model.response.YouTubeResult;

import java.util.List;

public interface YouTubeService {
    List<YouTubeResult> searchMultiple(String query, int maxResults);
}
