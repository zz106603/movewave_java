package com.movewave.emotion.service;

import com.movewave.emotion.model.response.EmotionResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class EmotionServiceImpl implements EmotionService {

    private final WebClient webClient;

    public EmotionServiceImpl(@Qualifier("flaskWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    @Retry(name = "flaskEmotion", fallbackMethod = "fallbackEmotion")
    @TimeLimiter(name = "flaskEmotion", fallbackMethod = "fallbackEmotion")
    @CircuitBreaker(name = "flaskEmotion", fallbackMethod = "fallbackEmotion")
    public CompletableFuture<EmotionResponse> analyzeEmotion(String text) {
        return webClient.post()
                .uri("/api/emotion/predict")
                .bodyValue(Map.of("text", text))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(result -> {
                    String prediction = String.valueOf(result.get("prediction"));
                    double confidence = ((Number) result.get("confidence")).doubleValue();
                    List<String> keywords = (List<String>) result.getOrDefault("keywords", List.of());
                    return new EmotionResponse(prediction, confidence, keywords);
                })
                .toFuture();
    }

    // fallbackMethod: 호출 실패 or circuit open 시 실행
    public CompletableFuture<EmotionResponse> fallbackEmotion(String text, Throwable t) {
        log.error("Flask 감정 분석 실패 (fallback): {}", t.getMessage());
        return CompletableFuture.completedFuture(new EmotionResponse("중립", 0.0, List.of("편안한 음악")));
    }
}
