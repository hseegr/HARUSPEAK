package com.haruspeak.api.today.presentation;

import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.today.application.MomentTagService;
import com.haruspeak.api.today.dto.request.MomentTagCreateRequest;
import com.haruspeak.api.today.dto.response.MomentTagCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/today")
@Tag(
        name = "Today",
        description = "오늘 순간 일기 관련 API"
)
public class MomentTagController {

    private final MomentTagService momentTagService;

    // 모먼트 자동생성
    @PostMapping("/tags")
    @Operation(summary = "AI 모먼트 태그 자동생성", description = "모먼트 태그를 AI로부터 추천 받아 자동 생성합니다.")
    public ResponseEntity<MomentTagCreateResponse> createMomentTag (
            @RequestBody MomentTagCreateRequest mtcr,
            @AuthenticatedUser Integer userId
    ) {
        String uri = "/ai/moment-tag";
        return ResponseEntity.ok(momentTagService.createMomentTag(uri, mtcr));
    }
}