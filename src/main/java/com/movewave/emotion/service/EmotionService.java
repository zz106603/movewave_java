package com.movewave.emotion.service;

import com.movewave.emotion.model.response.EmotionResponse;

import java.util.concurrent.CompletableFuture;

public interface EmotionService {
    /**
     * 텍스트의 감정을 분석하여 결과를 반환합니다.
     *
     * @param text 감정 분석할 텍스트
     * @param type 텍스트의 타입 (예: '힙합', '발라드' 등)
     * @return 감정 분석 결과를 포함한 CompletableFuture
     */
    CompletableFuture<EmotionResponse> analyzeEmotion(String text, String type);
}
