package com.movewave.emotion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.movewave.emotion.model.response.EmotionResponse;

import java.util.Map;

@FeignClient(name = "flaskClient", url = "http://localhost:3000")
public interface FlaskApiClient {

    @PostMapping(value = "/api/emotion/predict")
    EmotionResponse analyzeEmotion(@RequestBody Map<String, String> requestBody);
}
