package com.movewave.emotion.service;

import com.movewave.emotion.model.response.EmotionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmotionServiceImpl implements EmotionService {

    private final WebClient webClient;

    @Override
    public EmotionResponse analyzeEmotion(String text) {
        Map<String, String> requestBody = Map.of("text", text);

        Map<String, Object> result = webClient.post()
                .uri("/api/emotion/predict")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block(); // 동기 방식

        // 결과 파싱
        String prediction = String.valueOf(result.get("prediction"));
        double confidence = ((Number)result.get("confidence")).doubleValue();

        return new EmotionResponse(prediction, confidence);
    }
}
