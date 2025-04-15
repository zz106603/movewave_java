package com.movewave.favorite.model.request;

/**
 * 즐겨찾기 노래 요청 모델
 */
public record FavoriteSongRequest(
        String videoId,
        String title,
        String thumbnailUrl,
        String videoUrl,
        String musicUrl
) {}
