package com.movewave.youtube.model.response;

/**
 * YouTube 동영상의 썸네일 이미지 URL 정보를 담는 레코드 클래스입니다.
 * YouTubeThumbnails의 일부로 사용되며, 특정 크기(high, medium 등)의 썸네일 URL을 포함합니다.
 */
public record YouTubeThumbnail(
    /**
     * 썸네일 이미지의 URL
     */
    String url
) {}
