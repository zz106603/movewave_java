package com.movewave.emotion.model.response;

import java.util.List;

public record EmotionResponse(
        String prediction,
        Double confidence,
        List<String> keywords
) {
}
