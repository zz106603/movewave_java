package com.movewave.song.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "감정 분석 및 음악 추천 요청")
@Builder
public record SongRequest(
        @Schema(description = "분석할 텍스트", example = "오늘은 정말 행복한 하루였어!")
        @NotBlank(message = "텍스트를 입력해주세요")
        String text,

        @Schema(description = "텍스트 타입 (예: 힙합, 발라드 등)", example = "힙합") 
        @NotBlank(message = "텍스트 타입을 입력해주세요")
        String type
) {
}
