package com.haruspeak.batch.controller;

import com.haruspeak.batch.common.s3.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/util")
@RequiredArgsConstructor
@Tag(name = "UTIL", description = "각종 필요한 작업을 위한 유틸 API")
public class UtilController {

    private final S3Service s3Service;

    @PostMapping("/image")
    @Operation(
            summary = "이미지 업로드",
            description = "Base64로 변환된 이미지 업로드 후 S3에 저장된 URL을 반환"
    )
    public ResponseEntity<String> executeTodayDiaryJob(@RequestBody String base64) {
        log.info("🐛 [API 실행] 이미지 업로드 및 URL 요청");
        String url = s3Service.uploadImageAndGetUrl(base64);
        log.info("🐛 [API 실행] 이미지 업로드 완료");
        return ResponseEntity.ok(url);
    }

}
