package com.movewave.song.controller;

import com.movewave.song.model.request.SongRequest;
import com.movewave.song.model.response.SongResponse;
import com.movewave.song.service.SongService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    /**
     * 감정 분석 & 추천 음악 조회
     * @param request (text, type)
     * @return SongResponse (emotion, confidence, keyword, songs)
     */
    @PostMapping(SongApiUrls.MUSIC_URL)
    public SongResponse analyze(@RequestBody SongRequest request) {
        return songService.getRecommendSongs(request);
    }
}
