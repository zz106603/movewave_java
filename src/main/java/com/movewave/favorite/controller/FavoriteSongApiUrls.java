package com.movewave.favorite.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteSongApiUrls {
    public final static String FAVORITE_SONG_URL = "/api/favorite/song";
    public final static String FAVORITE_SONG_DELETE_URL = "/api/favorite/song/{videoId}";
    public final static String FAVORITE_SONG_PAGE_URL = "/api/favorite/song/page";
}
