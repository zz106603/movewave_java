package com.movewave.song.controller;

import com.movewave.song.model.request.SongRequest;
import com.movewave.song.model.response.SongResponse;
import com.movewave.song.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @PostMapping(SongApiUrls.MUSIC_URL)
    public SongResponse analyze(@RequestBody SongRequest request) {
        return songService.getRecommendSongs(request);
    }
}
