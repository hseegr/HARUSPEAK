package com.haruspeak.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info().title("HaruSpeak API")
                .description("일상을 기록하는 AI 다이어리")
                .version("v0.0.1") // 버전입력
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));
    }
}
