package com.movewave.song.model.response;

public record SongWithYoutube(
        String videoTitle,      // ex) "아이유 - 마음 (Official Audio)"
        String thumbnailUrl,
        String videoUrl,
        String videoId
) {}
