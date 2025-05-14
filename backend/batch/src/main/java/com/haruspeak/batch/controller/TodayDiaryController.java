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
public class TodayDiaryController {

    private final TodayDiaryJobRunner todayDiaryJobRunner;
    private final TodayDiaryJobStepRunner todayDiaryJobStepRunner;

    @PostMapping("/execute/diary/{date}")
    @Operation(
            summary = "특정 일자 일기 배치",
            description = "특정 일자에 대한 하루 일기 배치 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayDiaryJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 배치 요청 - DATE: {}", date);
        todayDiaryJobRunner.runTodayDiaryJob(date);
        log.info("🐛 [API 실행] 하루 일기 배치 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치 완료 - DATE: " + date);
    }

    @PostMapping("/execute/diary/{date}/save")
    @Operation(
            summary = "특정 일자 일기 배치(SAVE)",
            description = "특정 일자에 대한 하루 일기 배치(SAVE) 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayDiarySaveJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 배치(SAVE) 요청 - DATE: {}", date);
        todayDiaryJobRunner.runTodayDiarySaveJob(date);
        log.info("🐛 [API 실행] 하루 일기 배치(SAVE) 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치 완료 - DATE: " + date);
    }

    @PostMapping("/execute/diary/{date}/step/tag/start")
    @Operation(
            summary = "특정 일자 일기 배치 - START TAG STEP",
            description = "특정 일자에 대한 하루 일기 배치 작업 TAG 스텝부터 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayDiaryTagStepStartJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 Tag 스텝부터 배치 요청 - DATE: {}", date);
        todayDiaryJobRunner.runTodayDiaryTagStepStartJob(date);
        log.info("🐛 [API 실행] 하루 일기 Tag 스텝부터 배치 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 Tag 스텝부터 배치 완료 - DATE: " + date);
    }

    @PostMapping("/execute/diary/{date}/step/image/start")
    @Operation(
            summary = "특정 일자 일기 배치 - START IMAGE STEP",
            description = "특정 일자에 대한 하루 일기 배치 작업 IMAGE 스텝부터 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayDiaryImageStepStartJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 Image 스텝부터 배치 요청 - DATE: {}", date);
        todayDiaryJobRunner.runTodayDiaryImageStepStartJob(date);
        log.info("🐛 [API 실행] 하루 일기 Image 스텝부터 배치 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 Image 스텝부터 배치 완료 - DATE: " + date);
    }

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

    @PostMapping("/execute/diary/{date}/step/summary/user/{userId}")
    @Operation(
            summary = "특정 유저 특정 일자 하루 일기 배치 - SUMMARY STEP",
            description = "특정 유저의 특정 일자에 대한 하루 일기 배치 작업 SUMMARY STEP 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeDailySummaryStepByUserJob(@PathVariable String date, @PathVariable String userId) {
        log.info("🐛 [API 실행] 특정 유저 특정 일자 하루 일기 배치 - SUMMARY STEP 요청 - DATE: {}", date);
        todayDiaryJobStepRunner.runDailySummaryStepByUserJob(date, userId);
        log.info("🐛 [API 실행] 특정 유저 특정 일자 하루 일기 배치 - SUMMARY STEP 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 특정 유저 특정 일자 하루 일기 배치 - SUMMARY STEP 완료 - DATE: " + date);
    }

}
