package com.movewave.song.model.response;

import com.movewave.song.domain.Song;

public record SongWithYoutube(
        Song song,
        String thumbnailUrl,
        String videoUrl
) {}
