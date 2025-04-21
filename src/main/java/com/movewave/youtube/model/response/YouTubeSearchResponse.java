package com.movewave.youtube.model.response;

import java.util.List;

/**
 * YouTube Data API 검색 결과를 담는 레코드 클래스입니다.
 * YouTubeApiClient의 search 메서드 응답으로 사용됩니다.
 */
public record YouTubeSearchResponse(
    /**
     * 검색된 YouTube 동영상 목록
     * 각 항목은 동영상의 ID와 메타데이터(제목, 설명, 썸네일 등)를 포함합니다.
     */
    List<YouTubeItem> items
) {}
