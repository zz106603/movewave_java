package com.movewave.youtube.model.response;

import java.io.Serializable;

/**
 * YouTube 검색 결과의 개별 항목을 나타내는 레코드 클래스입니다.
 * 캐싱을 위해 Serializable을 구현합니다.
 */
public record YouTubeResult(
        /**
         * YouTube 동영상의 제목
         */
        String videoTitle,
        
        /**
         * 동영상 썸네일 이미지의 URL
         */
        String thumbnailUrl,
        
        /**
         * YouTube 동영상 시청 URL
         */
        String videoUrl,
        
        /**
         * YouTube Music 시청 URL
         */
        String musicUrl,
        
        /**
         * YouTube 동영상의 고유 식별자
         */
        String videoId
) implements Serializable {
}
