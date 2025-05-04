package com.haruspeak.api.user.presentation;

import com.haruspeak.api.common.exception.ErrorResponse;
import com.haruspeak.api.user.application.UserService;
import com.haruspeak.api.user.dto.CustomUserPrincipal;
import com.haruspeak.api.user.dto.response.LoginUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(
        name = "User",
        description = "사용자 정보 API"
)
public class UserController {

    @GetMapping("/me")
    @Operation(
            summary = "사용자 정보 요청",
            description = "인증(로그인)된 사용자의 userId, name을 반환합니다."
    )
    public ResponseEntity<LoginUserResponse> getCurrentUser(@AuthenticationPrincipal CustomUserPrincipal user) {
        log.info("[GET] api/user/me 로그인 사용자 정보 요청");
        return ResponseEntity.ok(new LoginUserResponse(user.getUserId(), user.getName()));
    }
}
