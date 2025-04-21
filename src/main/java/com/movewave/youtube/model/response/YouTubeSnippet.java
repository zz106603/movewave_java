package com.movewave.youtube.model.response;

/**
 * YouTube 동영상의 메타데이터를 담는 레코드 클래스입니다.
 * YouTubeItem의 일부로 사용되며, 동영상의 제목과 썸네일 정보를 포함합니다.
 */
public record YouTubeSnippet(
    /**
     * YouTube 동영상의 제목
     */
    String title,

    /**
     * 동영상의 썸네일 이미지 정보를 담고 있는 객체
     */
    YouTubeThumbnails thumbnails
) {}
