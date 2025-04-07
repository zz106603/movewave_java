package com.movewave.song.model.response;

import com.movewave.emotion.model.response.EmotionResponse;
import java.util.List;

public record SongResponse(
        String emotion,
        double confidence,
        List<SongData> songs
) {
    public static SongResponse from(EmotionResponse emotion, List<SongWithYoutube> songList) {
        List<SongData> songs = songList.stream()
                .map(s -> new SongData(
                        s.videoTitle(),
                        s.thumbnailUrl(),
                        s.videoUrl(),
                        s.videoId()
                ))
                .toList();

        return new SongResponse(emotion.prediction(), emotion.confidence(), songs);
    }

    private record SongData(
            String title,
            String thumbnailUrl,
            String videoUrl,
            String videoId) {}
}
