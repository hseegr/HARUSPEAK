package com.haruspeak.batch.controller;

import com.haruspeak.batch.runner.TodayDiaryJobRunner;
import com.haruspeak.batch.runner.TodayDiaryRetryJobRunner;
import com.haruspeak.batch.runner.TodayDiaryTargetUserJobRunner;
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
@RequestMapping("/batch/full")
@RequiredArgsConstructor
@Tag(name = "FULL JOB", description = "하루 일기 배치 작업 수동 실행 API")
public class TodayFullJobController {

    private final TodayDiaryJobRunner todayDiaryJobRunner;
    private final TodayDiaryTargetUserJobRunner todayDiaryTargetUserJobRunner;
    private final TodayDiaryRetryJobRunner todayDiaryRetryJobRunner;
    private final TodayThumbnailJobRunner todayThumbnailJobRunner;


    @PostMapping("/date/{date}/")
    @Operation(
            summary = "특정 일자 일기 배치 + 썸네일 생성",
            description = "특정 일자 일기 배치 + 썸네일 생성 배치 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayFullJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 특정 일자 일기 배치 + 썸네일 생성 요청 - DATE: {}", date);
        todayDiaryJobRunner.run(date);
        todayThumbnailJobRunner.run(date);
        log.info("🐛 [API 실행] 특정 일자 일기 배치 + 썸네일 생성 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 특정 일자 일기 배치 + 썸네일 생성 완료 - DATE: " + date);
    }

    @PostMapping("/date/{date}/retry")
    @Operation(
            summary = "특정 일자 일기 배치 + 썸네일 생성(RETRY)",
            description = "특정 일자 일기 배치 + 썸네일 생성 배치(RETRY) 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayRetryFullJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 특정 일자 일기 배치 + 썸네일 생성(RETRY) 요청 - DATE: {}", date);
        todayDiaryRetryJobRunner.run(date);
        todayThumbnailJobRunner.run(date);
        log.info("🐛 [API 실행] 특정 일자 일기 배치 + 썸네일 생성(RETRY) 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 특정 일자 일기 배치 + 썸네일 생성(RETRY) 완료 - DATE: " + date);
    }

    @PostMapping("/date/{date}/user/{userId}")
    @Operation(
            summary = "특정 일자/특정 유저 일기 배치 + 썸네일 생성",
            description = "특정 일자/특정 유저 일기 배치 + 썸네일 생성 배치 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayTargetUserFullJob(
            @PathVariable String date,
            @PathVariable String userId) {
        log.info("🐛 [API 실행] 특정 일자/특정 유저 일기 배치 + 썸네일 생성 요청 - DATE: {}", date);
        todayDiaryTargetUserJobRunner.run(userId, date);
        todayThumbnailJobRunner.run(date);
        log.info("🐛 [API 실행] 특정 일자/특정 유저 일기 배치 + 썸네일 생성 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 특정 일자/특정 유저 일기 배치 + 썸네일 생성 완료 - DATE: " + date);
    }

}
