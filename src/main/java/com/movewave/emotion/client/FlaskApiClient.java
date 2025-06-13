package com.movewave.emotion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.movewave.emotion.model.response.EmotionResponse;

import java.util.Map;

@FeignClient(name = "FlaskApiClient", url = "${fastapi.url}")
public interface FlaskApiClient {

    /**
     * 텍스트의 감정을 분석합니다.
     * @param requestBody 텍스트와 타입을 포함하는 Map
     * @return 감정 분석 결과를 포함하는 EmotionResponse
     */
    @PostMapping(value = "/api/emotion/predict")
    EmotionResponse analyzeEmotion(@RequestBody Map<String, String> requestBody);
}
