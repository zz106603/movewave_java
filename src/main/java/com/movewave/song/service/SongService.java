package com.movewave.song.service;

import com.movewave.song.model.request.SongRequest;
import com.movewave.song.model.response.SongResponse;

/**
 * 감정 분석 및 음악 추천 서비스 인터페이스
 */
public interface SongService {
    /**
     * 텍스트의 감정을 분석하고 해당 감정에 맞는 음악을 추천합니다.
     *
     * @param request 감정 분석을 위한 텍스트와 음악 타입이 포함된 요청 객체
     * @return 감정 분석 결과와 추천된 음악 목록
     */
    SongResponse getRecommendedSongs(SongRequest request);
}
