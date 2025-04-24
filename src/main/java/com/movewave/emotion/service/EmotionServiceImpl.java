package com.movewave.emotion.service;

import com.movewave.emotion.client.FlaskApiClient;
import com.movewave.emotion.model.response.EmotionResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.util.List;
import java.util.Map;
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
    
    /** Flask API 클라이언트 */
    private final FlaskApiClient flaskApiClient;
    
    /**
     * 텍스트의 감정을 분석합니다.
     * 장애 발생 시 재시도, 타임아웃, 서킷브레이커 패턴을 적용합니다.
     *
     * @param text 분석할 텍스트
     * @param type 텍스트 타입 (예: '힙합', '발라드' 등)
     * @return 감정 분석 결과를 포함한 EmotionResponse
     */
    @Override
    @Retryable(
        value = { Exception.class },       // 어떤 예외에서 재시도할지
        maxAttempts = 3,                   // 최대 재시도 횟수
        backoff = @Backoff(delay = 1000)   // 재시도 간 간격(ms)
    )
    public EmotionResponse analyzeEmotion(String text, String type) {
        try {
            return flaskApiClient.analyzeEmotion(createRequestBody(text, type));
        } catch (Exception e) {
            log.warn("Flask API 실패 → fallback 사용: {}", e.getMessage());
            return fallbackEmotion();
        }
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

    private EmotionResponse fallbackEmotion() {
        return new EmotionResponse(NEUTRAL_EMOTION, DEFAULT_CONFIDENCE, DEFAULT_KEYWORDS);
    }
}
