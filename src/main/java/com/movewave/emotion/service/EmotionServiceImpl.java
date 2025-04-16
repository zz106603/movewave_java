package com.movewave.emotion.service;

import com.movewave.emotion.model.response.EmotionResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 텍스트의 감정을 분석하는 서비스의 구현체입니다.
 * Flask 서버와 통신하여 감정 분석을 수행합니다.
 */
@Slf4j
@Service
public class EmotionServiceImpl implements EmotionService {

    private static final String NEUTRAL_EMOTION = "중립";
    private static final double DEFAULT_CONFIDENCE = 0.0;
    private static final List<String> DEFAULT_KEYWORDS = List.of("편안한 음악");
    private static final String EMOTION_API_PATH = "/api/emotion/predict";

    private final WebClient webClient;
    
    public EmotionServiceImpl(@Qualifier("flaskWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * 텍스트의 감정을 분석하여 결과를 반환합니다.
     * Flask 서버와의 통신에 실패할 경우 기본값을 반환합니다.
     *
     * @param text 감정 분석할 텍스트
     * @param type 텍스트의 타입 (예: '힙합', '발라드' 등)
     * @return 감정 분석 결과를 포함한 CompletableFuture
     */
    @Override
    @Retry(name = "flaskEmotion", fallbackMethod = "fallbackEmotion")
    @TimeLimiter(name = "flaskEmotion", fallbackMethod = "fallbackEmotion")
    @CircuitBreaker(name = "flaskEmotion", fallbackMethod = "fallbackEmotion")
    public CompletableFuture<EmotionResponse> analyzeEmotion(String text, String type) {
        return webClient.post()
                .uri(EMOTION_API_PATH)
                .bodyValue(createRequestBody(text, type))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(this::convertToEmotionResponse)
                .toFuture();
    }

    /**
     * Flask 서버로 전송할 요청 본문을 생성합니다.
     */
    private Map<String, String> createRequestBody(String text, String type) {
        return Map.of("text", text, "type", type);
    }

    /**
     * Flask 서버로부터 받은 응답을 EmotionResponse 객체로 변환합니다.
     */
    private EmotionResponse convertToEmotionResponse(Map<String, Object> result) {
        String prediction = String.valueOf(result.get("prediction"));
        double confidence = ((Number) result.get("confidence")).doubleValue();
        @SuppressWarnings("unchecked")
        List<String> keywords = (List<String>) result.getOrDefault("keywords", List.of());
        return new EmotionResponse(prediction, confidence, keywords);
    }

    /**
     * Flask 서버와의 통신에 실패했을 때 호출되는 폴백 메서드입니다.
     * 기본값으로 중립적인 감정과 낮은 신뢰도를 반환합니다.
     */
    public CompletableFuture<EmotionResponse> fallbackEmotion(String text, String type, Throwable t) {
        log.error("Flask 감정 분석 실패 (fallback): {}", t.getMessage());
        return CompletableFuture.completedFuture(
                new EmotionResponse(NEUTRAL_EMOTION, DEFAULT_CONFIDENCE, DEFAULT_KEYWORDS)
        );
    }
}
