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
@RequestMapping("/batch")
@RequiredArgsConstructor
@Tag(name = "JOB", description = "하루 일기 배치 작업 수동 실행 API")
public class TodayDiaryJobController {

    private final TodayDiaryJobRunner todayDiaryJobRunner;
    private final TodayDiaryTargetUserJobRunner todayDiaryTargetUserJobRunner;
    private final TodayDiaryRetryJobRunner todayDiaryRetryJobRunner;
    private final TodayThumbnailJobRunner todayThumbnailJobRunner;

    @PostMapping("/execute/diary/{date}")
    @Operation(
            summary = "특정 일자 일기 배치",
            description = "특정 일자에 대한 하루 일기 배치 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayDiaryJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 배치 요청 - DATE: {}", date);
        todayDiaryJobRunner.run(date);
        log.info("🐛 [API 실행] 하루 일기 배치 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치 완료 - DATE: " + date);
    }

    @PostMapping("/execute/diary/{date}/user/{userId}")
    @Operation(
            summary = "특정 일자 일기 배치",
            description = "특정 일자에 대한 하루 일기 배치 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayDiaryJob(
            @PathVariable String userId,
            @PathVariable String date
    ) {
        log.info("🐛 [API 실행] 하루 일기 배치 요청 - USERID: {}, DATE: {}", userId, date);
        todayDiaryTargetUserJobRunner.run(userId, date);
        log.info("🐛 [API 실행] 하루 일기 배치 완료 - USERID: {}, DATE: {}", userId, date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치 완료 - USERID: " + userId + "DATE: " + date);
    }

    @PostMapping("/execute/diary/{date}/retry")
    @Operation(
            summary = "특정 일자 일기 배치(RETRY)",
            description = "특정 일자에 대한 하루 일기 배치(RETRY) 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayDiaryRetryJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 배치(RETRY) 요청 - DATE: {}", date);
        todayDiaryRetryJobRunner.run(date);
        log.info("🐛 [API 실행] 하루 일기 배치(RETRY) 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치(RETRY) 완료 - DATE: " + date);
    }


    @PostMapping("/execute/thumbnail/{date}")
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
