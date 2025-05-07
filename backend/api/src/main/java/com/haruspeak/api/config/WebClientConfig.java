package com.haruspeak.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient fastApiWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8000")  // FastAPI 주소 (도커 포트면 그 주소)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB
                .build();
    }
}