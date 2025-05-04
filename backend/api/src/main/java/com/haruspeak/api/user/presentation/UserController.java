package com.haruspeak.api.user.presentation;

import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.common.security.CustomUserPrincipal;
import com.haruspeak.api.user.application.UserService;
import com.haruspeak.api.user.dto.UserTag;
import com.haruspeak.api.user.dto.response.LoginUserResponse;
import com.haruspeak.api.user.dto.response.UserTagResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 테스트 코드 미작성
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(
        name = "User",
        description = "사용자 정보 API"
)
public class UserController {

    private final UserService userService;

    /**
     * 사용자 정보 요청
     * @param user 인증된 사용자 
     * @return LoginUserResponse 인증된 사용자의 id, name
     */
    @GetMapping("/me")
    @Operation(
            summary = "사용자 정보 요청",
            description = "인증(로그인)된 사용자의 userId, name을 반환합니다."
    )
    public ResponseEntity<LoginUserResponse> getCurrentUser(@AuthenticationPrincipal CustomUserPrincipal user) {
        log.info("[GET] api/user/me 로그인 사용자 정보 요청 user={}", user);
        return ResponseEntity.ok(new LoginUserResponse(user.userId(), user.name()));
    }

    /**
     * 사용자의 태그 목록 불러오기
     * @param userId 사용자ID
     * @return
     */
    @GetMapping("/tags")
    @Operation(
            summary = "사용자 태그 정보 요청",
            description = "사용자가 일기에 사용했던 태그 목록을 불러옵니다."
    )
    public ResponseEntity<UserTagResponse> getUserTags(@AuthenticatedUser int userId) {
        log.info("[GET] api/user/tags 사용자 태그 목록 요청");
        List<UserTag> list = userService.getUserTags(userId);
        return ResponseEntity.ok(new UserTagResponse(list, list.size()));
    }
}
