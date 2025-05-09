package com.haruspeak.api.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class FastApiClient {

    private final WebClient fastApiWebClient;

    public FastApiClient(WebClient fastApiWebClient) {
        this.fastApiWebClient = fastApiWebClient;
    }

    public <T> T getPrediction(String uri, Object requestDto, Class<T> responseType) {
        return fastApiWebClient.post()
                .uri(uri) // "/ai/moment-tag";
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::isError,  // isError() 호출
                        this::handleError)
                .bodyToMono(responseType)
                .block(); // 동기 호출
    }

    private  Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(errorMessage -> {
                    log.error("Error response: " + errorMessage);
                    return Mono.error(new RuntimeException("FastAPI 호출 실패: " + errorMessage));
                });
    }
}
