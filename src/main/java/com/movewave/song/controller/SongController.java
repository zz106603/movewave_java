package com.movewave.song.controller;

import com.movewave.song.model.request.SongRequest;
import com.movewave.song.model.response.SongResponse;
import com.movewave.song.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/song")
@RequiredArgsConstructor
public class SongController implements SongApiDoc {

    private final SongService songService;

    /**
     * 감정 분석 & 추천 음악 조회
     * @param request 감정 분석을 위한 텍스트와 타입 정보를 담은 요청 객체
     * @return SongResponse 감정 분석 결과(emotion, confidence)와 추천 음악 목록(keyword, songs)
     */
    @PostMapping("")
    @Override
    public SongResponse analyzeSong(@RequestBody SongRequest request) {
        log.info("Analyzing emotion and recommending songs for request: {}", request);
        SongResponse response = songService.analyzeAndRecommend(request);
        log.debug("Analysis result: {}", response);
        return response;
    }
}
