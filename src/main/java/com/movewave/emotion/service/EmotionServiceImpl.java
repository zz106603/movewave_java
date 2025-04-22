package com.movewave.emotion.service;

import com.movewave.emotion.client.FlaskApiClient;
import com.movewave.emotion.model.response.EmotionResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;

/**
 * 감정 분석 서비스 구현 클래스입니다.
 * Flask 서버와 통신하여 텍스트의 감정을 분석합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmotionServiceImpl implements EmotionService {
    /** 기본 감정 상태 */
    private static final String NEUTRAL_EMOTION = "중립";
    /** 기본 신뢰도 값 */
    private static final double DEFAULT_CONFIDENCE = 0.0;
    /** 기본 키워드 목록 */
    private static final List<String> DEFAULT_KEYWORDS = List.of("편안한 음악");

    private static final String EMOTION_API_PATH = "/api/emotion/predict";
    
    /** Flask API 클라이언트 */
    private final FlaskApiClient flaskApiClient;
    
    /**
     * 텍스트의 감정을 분석합니다.
     * 장애 발생 시 재시도, 타임아웃, 서킷브레이커 패턴을 적용합니다.
     *
     * @param text 분석할 텍스트
     * @param type 텍스트 타입 (예: '힙합', '발라드' 등)
     * @return 감정 분석 결과를 포함한 CompletableFuture
     */
    @Override
    @Retry(name = "flaskEmotion", fallbackMethod = "fallbackEmotion")
    @TimeLimiter(name = "flaskEmotion", fallbackMethod = "fallbackEmotion")
    @CircuitBreaker(name = "flaskEmotion", fallbackMethod = "fallbackEmotion")
    public CompletableFuture<EmotionResponse> analyzeEmotion(String text, String type) {
        return flaskApiClient.post(
            EMOTION_API_PATH,
            createRequestBody(text, type),
            EmotionResponse.class
        );
    }
    

    /**
     * API 요청에 필요한 요청 본문을 생성합니다.
     * 
     * @param text 분석할 텍스트
     * @param type 분석 유형
     * @return 요청 본문 Map
     */
    private Map<String, String> createRequestBody(String text, String type) {
        return Map.of("text", text, "type", type);
    }
    
    /**
     * 감정 분석 실패 시 실행되는 폴백 메서드입니다.
     * 기본값을 포함한 EmotionResponse를 반환합니다.
     *
     * @param text 분석하려 했던 텍스트
     * @param type 텍스트 타입
     * @param t 발생한 예외
     * @return 기본 감정 분석 결과를 포함한 CompletableFuture
     */
    public CompletableFuture<EmotionResponse> fallbackEmotion(String text, String type, Throwable t) {
        log.error("Flask 감정 분석 실패 (fallback): {}", t.getMessage());
        return CompletableFuture.completedFuture(
                new EmotionResponse(NEUTRAL_EMOTION, DEFAULT_CONFIDENCE, DEFAULT_KEYWORDS)
        );
    }
}
