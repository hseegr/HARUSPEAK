package com.haruspeak.batch.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 외부 API 호출용
 */
@Configuration

public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);    // 연결 타임아웃: 5초
        factory.setReadTimeout(30000);  // 읽기 타임아웃: 30초 - 이미지 생성 약 20초에서 넉넉하게 30초로 설정

        return new RestTemplateBuilder()
                // 요청/응답 여러 번 읽을 수 있게 설정
                .requestFactory(() -> new BufferingClientHttpRequestFactory(factory))
                .build();
    }
}