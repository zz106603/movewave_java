package com.movewave.song.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * API URL 상수를 관리하는 유틸리티 클래스입니다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SongApiUrls {

    /**
     * 음악 추천 API 엔드포인트
     */
    public static final String MUSIC_URL = "/api/song";
}
