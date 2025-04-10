package com.movewave.emotion.service;

import com.movewave.emotion.model.response.EmotionResponse;

import java.util.concurrent.CompletableFuture;

public interface EmotionService {
    public CompletableFuture<EmotionResponse> analyzeEmotion(String text, String type);
}
