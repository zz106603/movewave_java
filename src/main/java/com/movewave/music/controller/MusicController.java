package com.movewave.music.controller;

import com.movewave.music.model.request.MusicRequest;
import com.movewave.music.model.response.MusicResponse;
import com.movewave.music.service.MusicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MusicController {

    private final MusicService musicService;

    @PostMapping(MusicApiUrls.MUSIC_URL)
    public List<MusicResponse> analyze(@RequestBody MusicRequest request) {
        return musicService.getRecommendSongs(request);
    }
}
