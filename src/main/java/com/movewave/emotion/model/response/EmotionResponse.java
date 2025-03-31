package com.movewave.emotion.model.response;

public record EmotionResponse(
        String prediction,
        Double confidence
) {
}
