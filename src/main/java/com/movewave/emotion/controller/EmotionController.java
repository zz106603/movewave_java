package com.movewave.emotion.controller;

import com.movewave.emotion.model.request.EmotionRequest;
import com.movewave.emotion.model.response.EmotionResponse;
import com.movewave.emotion.service.EmotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;

    @PostMapping(EmotionApiUrls.MUSIC_URL)
    public EmotionResponse analyze(@RequestBody EmotionRequest request) {
        return emotionService.analyzeEmotion(request);
    }

}
