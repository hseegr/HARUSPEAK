package com.haruspeak.api.user.presentation;

import com.haruspeak.api.user.application.UserService;
import com.haruspeak.api.user.dto.CustomUserPrincipal;
import com.haruspeak.api.user.dto.response.LoginUserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "사용자 관련 API", description = "사용자 정보, 사용자 태그 불러오기")
public class UserController {


    @GetMapping("/me")
    public ResponseEntity<LoginUserResponse> getCurrentUser(@AuthenticationPrincipal CustomUserPrincipal user) {
        return ResponseEntity.ok(new LoginUserResponse(user.getUserId(), user.getName()));
    }
}
