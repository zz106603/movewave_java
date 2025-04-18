package com.movewave.youtube.model.response;

/**
 * YouTube 동영상의 ID 정보를 담는 레코드 클래스입니다.
 * YouTube Data API 응답의 일부로 사용됩니다.
 */
public record YouTubeId(
    /**
     * YouTube 동영상의 고유 식별자
     * 예: "dQw4w9WgXcQ"
     */
    String videoId
) {}
