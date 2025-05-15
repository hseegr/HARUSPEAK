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
public class TodayDiaryJobController {

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
