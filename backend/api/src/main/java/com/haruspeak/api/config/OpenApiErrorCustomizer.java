package com.haruspeak.api.config;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 문서에 작성할 에러 커스텀
 */
@Slf4j
@Configuration
public class OpenApiErrorCustomizer {

    @Bean
    public GroupedOpenApi globalApiCustomizer() {
        return GroupedOpenApi.builder()
                .group("global") // swagger-ui에서 구분 이름
                .pathsToMatch("/**") // 전체 경로 적용
                .addOpenApiCustomizer(openApi -> {
                    openApi.getPaths().values().forEach(pathItem -> {
                        pathItem.readOperations().forEach(operation -> {
                            String controllerClass = operation.getTags().stream().findFirst().orElse("");
                            log.debug("🧪 controllerClass: {}", controllerClass);

                            // 200: 전체에 적용
                            addErrorResponse(operation.getResponses(), "200", "OK");

                            // 401: 모든 컨트롤러에 적용(OAuthLoginController 제외 - 비인증 클래스: refreshToken 따로 적용)
                            if (!controllerClass.equals("Auth")) {
                                addErrorResponse(operation.getResponses(), "401", "인증 실패");
                            }

                            // 403: 특정 컨트롤러에만 적용
                            if (controllerClass.equals("AdminController") || controllerClass.equals("UserController")) {
                                addErrorResponse(operation.getResponses(), "403", "권한 없음");
                            }

                            // 500: 전체에 적용
                            addErrorResponse(operation.getResponses(), "500", "서버 내부 오류");
                        });
                    });
                })
                .build();
    }


    /**
     * 공통 응답 추가
     */
    private void addErrorResponse(ApiResponses responses, String code, String description) {
        if (!responses.containsKey(code)) {
            responses.addApiResponse(code, new ApiResponse()
                    .description(description)
                    .content(new Content().addMediaType("application/json",
                            new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))));
        }
    }

}
