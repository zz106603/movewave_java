package com.movewave.youtube.service;

import com.movewave.youtube.model.response.YouTubeResult;

import java.util.List;

/**
 * YouTube 검색 기능을 제공하는 서비스 인터페이스입니다.
 */
public interface YouTubeService {
    /**
     * 주어진 검색어로 YouTube 동영상을 검색합니다.
     *
     * @param query 검색할 키워드
     * @param maxResults 검색 결과의 최대 개수 (1-50 사이의 값)
     * @return 검색된 동영상 정보 목록
     * @throws IllegalArgumentException
     */
    List<YouTubeResult> searchMultiple(String query, int maxResults);
}
