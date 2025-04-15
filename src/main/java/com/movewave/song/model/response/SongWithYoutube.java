package com.movewave.song.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 유튜브 음악 정보를 담는 레코드 클래스입니다.
 */
@Schema(description = "유튜브 음악 정보")
public record SongWithYoutube(
        @Schema(description = "영상 제목", example = "행복한 하루에 어울리는 Playlist")
        String videoTitle,
        
        @Schema(description = "썸네일 이미지 URL", example = "https://i.ytimg.com/vi/...")
        String thumbnailUrl,
        
        @Schema(description = "유튜브 영상 URL", example = "https://www.youtube.com/watch?v=...")
        String videoUrl,
        
        @Schema(description = "유튜브 뮤직 URL", example = "https://music.youtube.com/watch?v=...")
        String musicUrl,
        
        @Schema(description = "유튜브 비디오 ID", example = "dQw4w9WgXcQ")
        String videoId
) {}
