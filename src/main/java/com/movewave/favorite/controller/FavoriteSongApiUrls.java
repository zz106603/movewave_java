package com.movewave.favorite.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 즐겨찾기 노래 관련 API URL 상수 클래스
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteSongApiUrls {
    /**
     * 즐겨찾기 노래 기본 URL
     */
    public static final String FAVORITE_SONG_URL = "/api/favorite/song";

    /**
     * 즐겨찾기 노래 삭제 URL
     * {videoId}: 삭제할 노래의 비디오 ID
     */
    public static final String FAVORITE_SONG_DELETE_URL = "/api/favorite/song/{videoId}";

    /**
     * 즐겨찾기 노래 페이징 조회 URL
     */
    public static final String FAVORITE_SONG_PAGE_URL = "/api/favorite/song/page";
}
