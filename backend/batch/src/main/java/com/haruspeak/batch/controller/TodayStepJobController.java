package com.haruspeak.batch.controller;

import com.haruspeak.batch.runner.TodayStepJobRunner;
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
@Tag(name = "STEP", description = "하루 일기 배치 작업(STEP) 수동 실행 API")
public class TodayStepJobController {

    private final TodayStepJobRunner jobRunner;


    @PostMapping("/execute/diary/{date}/step")
    @Operation(
            summary = "[STEP] 특정 일자 일기 배치",
            description = "[STEP] 특정 일자에 대한 하루 일기 배치 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayDiaryJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 STEP 배치 요청  - DATE: {}", date);
        jobRunner.runTodayDiaryStepJob(date);
        log.info("🐛 [API 실행] 하루 일기 STEP 배치 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치 완료 - DATE: " + date);
    }

    @PostMapping("/execute/diary/{date}/retry/step")
    @Operation(
            summary = "[STEP] 특정 일자 일기 배치(RETRY)",
            description = "[STEP] 특정 일자에 대한 하루 일기 배치(RETRY) 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayDiaryRetryJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 STEP 배치(RETRY) 요청 - DATE: {}", date);
        jobRunner.runTodayDiaryRetryStepJob(date);
        log.info("🐛 [API 실행] 하루 일기 STEP 배치(RETRY) 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 배치(RETRY) 완료 - DATE: " + date);
    }

    @PostMapping("/execute/tag/{date}/step")
    @Operation(
            summary = "[STEP] 특정 일자 태그 배치",
            description = "[STEP] 특정 일자에 대한 태그 배치 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayTagJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 태그 STEP 배치 요청 - DATE: {}", date);
        jobRunner.runTodayTagStepJob(date);
        log.info("🐛 [API 실행] 하루 일기 태그 STEP 배치 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 태그 배치 완료 - DATE: " + date);
    }

    @PostMapping("/execute/image/{date}/step")
    @Operation(
            summary = "[STEP] 특정 일자 이미지 배치",
            description = "[STEP] 특정 일자에 대한 이미지 작업 실행(YYYY-MM-dd)"
    )
    public ResponseEntity<String> executeTodayDiarySaveJob(@PathVariable String date) {
        log.info("🐛 [API 실행] 하루 일기 이미지 STEP 배치 요청 - DATE: {}", date);
        jobRunner.runTodayImageStepJob(date);
        log.info("🐛 [API 실행] 하루 일기 이미지 STEP 배치 완료 - DATE: {}", date);
        return ResponseEntity.ok("🐛 [API 실행] 하루 일기 이미지 배치 완료 - DATE: " + date);
    }



}
