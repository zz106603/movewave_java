package com.movewave.song.model.response;

import com.movewave.emotion.model.response.EmotionResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "감정 분석 및 음악 추천 응답")
public record SongResponse(
        @Schema(description = "분석된 감정", example = "행복")
        String emotion,
        
        @Schema(description = "감정 분석 신뢰도", example = "0.95")
        double confidence,
        
        @Schema(description = "추천 음악 키워드", example = "힙합")
        String keyword,
        
        @Schema(description = "추천된 음악 목록")
        List<SongData> songs
) {
    public static SongResponse from(EmotionResponse emotion, String keyword, List<SongWithYoutube> songList) {
        List<SongData> songs = songList.stream()
                .map(s -> new SongData(
                        s.videoTitle(),
                        s.thumbnailUrl(),
                        s.videoUrl(),
                        s.musicUrl(),
                        s.videoId()
                ))
                .toList();

        return new SongResponse(emotion.prediction(), emotion.confidence(), keyword, songs);
    }

    @Schema(description = "추천 음악 상세 정보")
    public record SongData(
            @Schema(description = "음악 제목", example = "행복한 하루에 어울리는 Playlist")
            String title,
            
            @Schema(description = "썸네일 URL")
            String thumbnailUrl,
            
            @Schema(description = "유튜브 영상 URL")
            String videoUrl,
            
            @Schema(description = "유튜브 뮤직 URL")
            String musicUrl,
            
            @Schema(description = "유튜브 비디오 ID")
            String videoId) {}
}
