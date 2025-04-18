package com.movewave.emotion.service;

import com.movewave.emotion.model.response.EmotionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmotionServiceImplTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private EmotionServiceImpl emotionService;

    private static final String TEST_TEXT = "테스트 텍스트";
    private static final String TEST_TYPE = "힙합";
    private static final String TEST_EMOTION = "기쁨";
    private static final double TEST_CONFIDENCE = 0.85;
    private static final List<String> TEST_KEYWORDS = List.of("신나는", "댄스");
    private static final String EMOTION_API_PATH = "/api/emotion/predict";

    @Test
    @DisplayName("정상적인 감정 분석 응답 처리")
    void analyzeEmotion_Success() {
        // given
        Map<String, Object> mockResponse = Map.of(
                "prediction", TEST_EMOTION,
                "confidence", TEST_CONFIDENCE,
                "keywords", TEST_KEYWORDS
        );
        
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq(EMOTION_API_PATH))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn((WebClient.RequestHeadersSpec) requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(ParameterizedTypeReference.class))).thenReturn(Mono.just(mockResponse));

        // when
        CompletableFuture<EmotionResponse> result = emotionService.analyzeEmotion(TEST_TEXT, TEST_TYPE);

        // then
        assertThat(result).isCompleted();
        EmotionResponse response = result.join();
        assertThat(response.prediction()).isEqualTo(TEST_EMOTION);
        assertThat(response.confidence()).isEqualTo(TEST_CONFIDENCE);
        assertThat(response.keywords()).isEqualTo(TEST_KEYWORDS);
    }

    @Test
    @DisplayName("Flask 서버 통신 실패 시 폴백 메서드 호출")
    void fallbackEmotion_Success() {
        // given
        RuntimeException exception = new RuntimeException("Flask 서버 통신 실패");

        // when
        CompletableFuture<EmotionResponse> result = emotionService.fallbackEmotion(TEST_TEXT, TEST_TYPE, exception);

        // then
        assertThat(result).isCompleted();
        EmotionResponse response = result.join();
        assertThat(response.prediction()).isEqualTo("중립");
        assertThat(response.confidence()).isEqualTo(0.0);
        assertThat(response.keywords()).containsExactly("편안한 음악");
    }
} 
