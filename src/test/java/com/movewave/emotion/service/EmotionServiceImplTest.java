package com.movewave.emotion.service;

import com.movewave.emotion.client.FlaskApiClient;
import com.movewave.emotion.model.response.EmotionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmotionServiceImplTest {

    @Mock
    private FlaskApiClient flaskApiClient;

    @InjectMocks
    private EmotionServiceImpl emotionService;

    private static final String TEST_TEXT = "테스트 텍스트";
    private static final String TEST_TYPE = "힙합";
    private static final String TEST_EMOTION = "기쁨";
    private static final double TEST_CONFIDENCE = 0.85;
    private static final List<String> TEST_KEYWORDS = List.of("신나는", "댄스");

    @Test
    @DisplayName("정상적인 감정 분석 응답 처리")
    void analyzeEmotion_Success() {
        // given
        EmotionResponse expected = new EmotionResponse(TEST_EMOTION, TEST_CONFIDENCE, TEST_KEYWORDS);

        when(flaskApiClient.analyzeEmotion(anyMap())).thenReturn(expected);

        // when
        EmotionResponse response = emotionService.analyzeEmotion(TEST_TEXT, TEST_TYPE);

        // then
        assertThat(response.prediction()).isEqualTo(TEST_EMOTION);
        assertThat(response.confidence()).isEqualTo(TEST_CONFIDENCE);
        assertThat(response.keywords()).isEqualTo(TEST_KEYWORDS);
    }
} 
