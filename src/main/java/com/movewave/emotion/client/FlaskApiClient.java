package com.movewave.emotion.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Component
public class FlaskApiClient {

    private final WebClient webClient;

    public FlaskApiClient(@Qualifier("flaskWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> CompletableFuture<T> post(
            String uri,
            Object requestBody,
            Class<T> responseType
    ) {
        return webClient.post()
                .uri(uri)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType)
                .toFuture();
    }
}
