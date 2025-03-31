package com.movewave.emotion.service;

import com.movewave.emotion.model.request.EmotionRequest;
import com.movewave.emotion.model.response.EmotionResponse;

public interface EmotionService {
    public EmotionResponse analyzeEmotion(EmotionRequest request);
}
