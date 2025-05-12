package com.haruspeak.api.user.presentation;

import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.user.application.UserStatService;
import com.haruspeak.api.user.dto.response.LoginUserResponse;
import com.haruspeak.api.user.dto.response.UserStatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
@Tag(
        name = "User"
)
public class UserStatController {

    private final UserStatService userStatService;

    /**
     * 사용자 메인 SATAT 조회
     * @param userId 사용자ID
     * @return UserStatResponse 사용자 stat 정보
     */
    @GetMapping("")
    @Operation(
            summary = "사용자 메인 스탯 요청",
            description = "사용자의 오늘 작성 일기 수, 삭제되지 않은 총 순간 일기 수, 총 하루 일기 수를 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(implementation = UserStatResponse.class))
                    )
            }
    )
    public ResponseEntity<UserStatResponse> getUserTags(@AuthenticatedUser Integer userId) {
        log.info("[GET] api/main 사용자 스탯 조회 (userId={})", userId);
        return ResponseEntity.ok(userStatService.getUserStat(userId));
    }
}
