package com.haruspeak.api.moment.presentation;

import com.haruspeak.api.common.security.AuthenticatedUser;
import com.haruspeak.api.moment.application.MomentTagService;
import com.haruspeak.api.moment.dto.request.MomentTagCreateRequest;
import com.haruspeak.api.moment.dto.response.MomentTagCreateResponse;
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
@Tag(name = "MomentTag", description = "모먼트 태그 API")
public class MomentTagController {
    private final MomentTagService momentTagService;

    @PostMapping("/tags")
    @Operation(summary = "AI 모먼트 태그 자동생성", description = "모먼트 태그를 AI 로부터 추천받아 자동생성합니다.")
    public ResponseEntity<MomentTagCreateResponse> createMomentTag (
            @RequestBody(required = true) MomentTagCreateRequest mtcr
//            @AuthenticatedUser Integer userId
    ) {
        String uri = "/ai/moment-tag";
        return ResponseEntity.ok(momentTagService.createMomentTag(uri, mtcr));
    }
}