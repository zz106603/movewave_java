package com.movewave.song.service;

import com.movewave.song.model.response.YouTubeResult;

public interface YouTubeService {
    public YouTubeResult search(String title, String artist);
}
