package com.haruspeak.api.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class FastApiClient {

    private final WebClient fastApiWebClient;

    public FastApiClient(WebClient fastApiWebClient) {
        this.fastApiWebClient = fastApiWebClient;
    }

    public String getPrediction(String uri, Object requestDto) {

        System.out.println("####" + requestDto.toString());

        return fastApiWebClient.post()
                .uri(uri) // "/ai/moment-tag";
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // 동기 호출
    }
}
