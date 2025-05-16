package com.haruspeak.api.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class FastApiClient {

    private final WebClient fastApiWebClient;
    private final WebClient gpuSttWebClient;

    public FastApiClient(
            @Qualifier("fastApiWebClient") WebClient fastApiWebClient,
            @Qualifier("gpuSttWebClient") WebClient gpuSttWebClient
    ) {
        this.fastApiWebClient = fastApiWebClient;
        this.gpuSttWebClient = gpuSttWebClient;
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

    public <T> T gpuConvertVoiceToText(String uri, MultipartFile file, Class<T> responseType) {
        return gpuSttWebClient.post()
                .uri(uri)
                .contentType(MediaType.MULTIPART_FORM_DATA) // multipart/form-data로 설정
                .body(BodyInserters.fromMultipartData("file", file.getResource())) // 파일만 멀티파트로 전송
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(responseType)
                .block(); // 동기 호출
    }

    public <T> T convertVoiceToText(String uri, MultipartFile file, Class<T> responseType) {
        return fastApiWebClient.post()
                .uri(uri)
                .contentType(MediaType.MULTIPART_FORM_DATA) // multipart/form-data로 설정
                .body(BodyInserters.fromMultipartData("file", file.getResource())) // 파일만 멀티파트로 전송
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
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
