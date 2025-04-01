package com.movewave.song.model.response;

import com.movewave.emotion.model.response.EmotionResponse;
import com.movewave.song.domain.Song;
import java.util.List;

public record SongResponse(
        String emotion,
        double confidence,
        List<SongData> songs
) {
    public static SongResponse from(EmotionResponse emotion, List<Song> songList) {
        List<SongData> songs = songList.stream()
                .map(song -> new SongData(song.getTitle(), song.getArtist()))
                .toList();

        return new SongResponse(emotion.prediction(), emotion.confidence(), songs);
    }

    private record SongData(String title, String artist) {}
}
