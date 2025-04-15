package com.movewave.favorite.model.response;

/**
 * 즐겨찾기 노래 응답 모델
 */
public record FavoriteSongResponse(
        String videoId,
        String title,
        String thumbnailUrl,
        String videoUrl,
        String musicUrl
) {}
