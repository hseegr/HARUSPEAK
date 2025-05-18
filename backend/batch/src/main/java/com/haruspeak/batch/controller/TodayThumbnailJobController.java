package com.haruspeak.batch.controller;

import com.haruspeak.batch.runner.TodayThumbnailJobRunner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/batch/thumbnail")
@RequiredArgsConstructor
@Tag(name = "THUMBNAIL JOB", description = "일기 썸네일 배치 작업 수동 실행 API")
public class TodayThumbnailJobController {

    private final TodayThumbnailJobRunner todayThumbnailJobRunner;

    @PostMapping("/date/{date}")
    @Operation(
            summary = "특정 일자 썸네일 배치",
            description = "특정 일자에 대한 썸네일 배치 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayThumbnailJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 특정 일자에 대한 썸네일 배치 요청 - DATE: {}", date);
        todayThumbnailJobRunner.run(date);
        log.info("🐛 [API 실행]  특정 일자에 대한 썸네일 배치 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행]  특정 일자에 대한 썸네일 배치 완료 - DATE: " + date);
    }
}
