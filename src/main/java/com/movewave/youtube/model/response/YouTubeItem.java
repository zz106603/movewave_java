package com.movewave.youtube.model.response;

/**
 * YouTube Data API 검색 결과의 개별 아이템을 나타내는 레코드 클래스입니다.
 */
public record YouTubeItem(
    /**
     * 동영상의 ID 정보를 담고 있는 객체
     */
    YouTubeId id,

    /**
     * 동영상의 메타데이터(제목, 설명, 썸네일 등)를 담고 있는 객체
     */
    YouTubeSnippet snippet
) {}
