package com.movewave.favorite.model.request;

public record FavoriteSongRequest(
        String videoId,
        String title,
        String thumbnailUrl,
        String videoUrl,
        String musicUrl
) {}