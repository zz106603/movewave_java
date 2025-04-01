package com.movewave.music.model.response;

import com.movewave.music.domain.Song;
import java.util.List;

public record MusicResponse(
        String title,
        String artist,
        String emotion) {

    public static MusicResponse from(Song song) {
        return new MusicResponse(
                song.getTitle(),
                song.getArtist(),
                song.getEmotion());
    }

    public static List<MusicResponse> from(List<Song> songs) {
        return songs.stream()
                .map(MusicResponse::from)
                .toList();
    }
}
