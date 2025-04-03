package com.movewave.song.service;

import com.movewave.song.model.response.YouTubeResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YouTubeServiceImpl implements YouTubeService{
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://www.googleapis.com/youtube/v3")
            .build();

    private final String API_KEY = "AIzaSyAqTEiVijGjOqxp_cchARlY8hI-CkERORw";

    @Override
    public YouTubeResult search(String query) {
        String uri = UriComponentsBuilder.fromPath("/search")
                .queryParam("part", "snippet")
                .queryParam("q", query)
                .queryParam("type", "video")
                .queryParam("maxResults", 1)
                .queryParam("key", API_KEY)
                .build()
                .toUriString();

        try {
            Map<String, Object> response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(); // 블로킹 호출로 동기화

            if (response != null && response.containsKey("items")) {
                List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
                if (!items.isEmpty()) {
                    Map<String, Object> item = items.get(0);
                    Map<String, Object> id = (Map<String, Object>) item.get("id");
                    Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
                    Map<String, Object> thumbnails = (Map<String, Object>) ((Map<String, Object>) snippet.get("thumbnails")).get("high");

                    String thumbnailUrl = (String) thumbnails.get("url");
                    String videoId = (String) id.get("videoId");
                    String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

                    return new YouTubeResult(query, thumbnailUrl, videoUrl);
                }
            }
        } catch (Exception e) {
            System.out.println("YouTube API 호출 실패: " + e.getMessage());
        }

        return new YouTubeResult(query, "", "");
    }
}
