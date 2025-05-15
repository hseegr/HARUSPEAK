package com.haruspeak.batch.controller;

import com.haruspeak.batch.runner.TodayDiaryJobRunner;
import com.haruspeak.batch.runner.TodayDiaryJobStepRunner;
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
@Tag(name = "TodayDiary", description = "하루 일기 배치 작업 수동 실행 API")
public class TodayDiaryStepJobController {

    private final TodayDiaryJobRunner todayDiaryJobRunner;
    private final TodayDiaryJobStepRunner todayDiaryJobStepRunner;

    @PostMapping("/execute/diary/{date}/step/summary")
    @Operation(
            summary = "특정 일자 일기 배치 - SUMMARY STEP",
            description = "특정 일자에 대한 하루 일기 배치 작업 SUMMARY STEP 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeDailySummaryStepJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 배치 Summary STEP 요청 - DATE: {}", date);
        todayDiaryJobStepRunner.runDailySummaryStepJob(date);
        log.info("🐛 [API 실행] 하루 일기 배치 Summary STEP 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치 Summary STEP 완료 - DATE: " + date);
    }

    @PostMapping("/execute/diary/{date}/step/tag")
    @Operation(
            summary = "특정 일자 일기 배치 - TAG STEP",
            description = "특정 일자에 대한 하루 일기 배치 작업 TAG STEP 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayTagStepJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 배치 Tag STEP 요청 - DATE: {}", date);
        todayDiaryJobStepRunner.runTodayDiaryTagStepJob(date);
        log.info("🐛 [API 실행] 하루 일기 배치 Tag STEP 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치 Tag STEP 완료 - DATE: " + date);
    }

    @PostMapping("/execute/diary/{date}/step/image")
    @Operation(
            summary = "특정 일자 일기 배치 - IMAGE STEP",
            description = "특정 일자에 대한 하루 일기 배치 작업 IMAGE STEP 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayImageStepJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 배치 Image STEP 요청 - DATE: {}", date);
        todayDiaryJobStepRunner.runTodayDiaryImageStepJob(date);
        log.info("🐛 [API 실행] 하루 일기 배치 Image STEP 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치 Image STEP 완료 - DATE: " + date);
    }

    @PostMapping("/execute/diary/{date}/step/thumbnail")
    @Operation(
            summary = "특정 일자 일기 배치 - THUMBNAIL STEP",
            description = "특정 일자에 대한 하루 일기 배치 작업 THUMBNAIL STEP 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayThumbnailStepJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 배치 THUMBNAIL STEP 요청 - DATE: {}", date);
        todayDiaryJobStepRunner.runTodayThumbnailStepJob(date);
        log.info("🐛 [API 실행] 하루 일기 배치 THUMBNAIL STEP 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치 THUMBNAIL STEP 완료 - DATE: " + date);
    }



}
