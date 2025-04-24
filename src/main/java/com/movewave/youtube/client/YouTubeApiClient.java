package com.movewave.youtube.client;

import com.movewave.youtube.model.response.YouTubeSearchResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.cloud.openfeign.FeignClient;
/**
 * YouTube Data API와 통신하는 클라이언트 클래스입니다.
 */
@FeignClient(name = "youtubeApiClient", url = "https://www.googleapis.com/youtube/v3")
public interface YouTubeApiClient {

    @GetMapping(value = "/search")
    YouTubeSearchResponse searchVideos(
        @RequestParam("part") String part,
        @RequestParam("q") String query, 
        @RequestParam("type") String type,
        @RequestParam("maxResults") int maxResults,
        @RequestParam("key") String apiKey
        );
}
