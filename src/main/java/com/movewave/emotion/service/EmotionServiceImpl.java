package com.movewave.emotion.service;

import com.movewave.emotion.model.response.EmotionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmotionServiceImpl implements EmotionService {

    @Override
    public EmotionResponse analyzeEmotion(String text) {
        String flaskUrl = "http://localhost:5000/api/emotion/predict";

        RestTemplate restTemplate = new RestTemplate();

        // Flask에 보낼 JSON body
        Map<String, String> body = new HashMap<>();
        body.put("text", text);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 객체 생성
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // Flask 서버로 요청 보내고 결과 받기
        ResponseEntity<Map> response = restTemplate.exchange(
                flaskUrl,
                HttpMethod.POST,
                entity,
                Map.class
        );

        // 결과 파싱
        Map result = response.getBody();
        String prediction = (String) result.get("prediction");
        Double confidence = ((Number) result.get("confidence")).doubleValue();

        return new EmotionResponse(prediction, confidence);
    }
}
