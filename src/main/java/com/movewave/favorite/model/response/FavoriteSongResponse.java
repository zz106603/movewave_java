package com.movewave.favorite.model.response;

public record FavoriteSongResponse(
        String videoId,
        String title,
        String thumbnailUrl,
        String videoUrl,
        String musicUrl
) {}
