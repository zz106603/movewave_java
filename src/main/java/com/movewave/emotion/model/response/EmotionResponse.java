package com.movewave.emotion.model.response;

import java.util.List;

/**
 * 감정 분석 결과를 담는 응답 객체입니다.
 */
public record EmotionResponse(
        /**
         * 예측된 감정 (예: "기쁨", "슬픔", "분노" 등)
         */
        String prediction,
        
        /**
         * 예측에 대한 신뢰도 (0.0 ~ 1.0)
         */
        Double confidence,
        
        /**
         * 텍스트에서 추출된 주요 키워드 목록
         */
        List<String> keywords
) {
}
