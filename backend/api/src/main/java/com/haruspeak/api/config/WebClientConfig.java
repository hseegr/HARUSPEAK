package com.haruspeak.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("fastApiWebClient")
    public WebClient fastApiWebClient(
            @Value("${AI_BASE_URL}") String baseUrl
    ) {
        return WebClient.builder()
                .baseUrl(baseUrl)  // FastAPI 주소 (도커 포트면 그 주소)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                .build();
    }

    @Bean
    @Qualifier("gpuSttWebClient")
    public WebClient gpuSttWebClient(
            @Value("${AI_STT_BASE_URL}") String sttApiBaseUrl
    ) {
        return WebClient.builder()
                .baseUrl(sttApiBaseUrl)  // GPU 서버에서 이미지 생성
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(20 * 1024 * 1024))
                .build();
    }
}