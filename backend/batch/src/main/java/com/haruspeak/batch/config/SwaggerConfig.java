package com.haruspeak.batch.config;

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
        return new Info().title("Haru's peak Batch API")
                .description("Haru's peak 수동 배치 작업 API")
                .version("v0.0.1") // 버전입력
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));
    }
}

