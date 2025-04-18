package com.movewave.youtube.model.response;

/**
 * YouTube 동영상의 썸네일 이미지 정보를 담는 레코드 클래스입니다.
 * YouTubeSnippet의 일부로 사용되며, 다양한 크기의 썸네일 정보를 포함합니다.
 */
public record YouTubeThumbnails(
    /**
     * 고화질(high quality) 썸네일 이미지 정보
     */
    YouTubeThumbnail high
) {}
