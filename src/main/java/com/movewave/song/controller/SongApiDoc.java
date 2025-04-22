package com.movewave.song.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.movewave.song.model.response.SongResponse;
import com.movewave.song.model.request.SongRequest;
import com.movewave.common.model.response.SwaggerCommonResponses;

@Tag(name = "Song", description = "음악 추천 관련 API")
@SecurityRequirement(name = "bearerAuth")
@SwaggerCommonResponses
public interface SongApiDoc {

    /**
     * 텍스트 기반 감정 분석 후 음악을 추천합니다.
     * @param request 분석할 텍스트와 타입 정보
     * @return 감정 분석 결과와 추천 음악 목록
     */
    @Operation(
        summary = "감정 분석 & 음악 추천",
        description = "텍스트를 분석하여 감정을 파악하고 이에 맞는 음악을 추천합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "성공적으로 감정을 분석하고 음악을 추천했습니다."
            )
        }
    )
    SongResponse analyzeSong(SongRequest request);

}
