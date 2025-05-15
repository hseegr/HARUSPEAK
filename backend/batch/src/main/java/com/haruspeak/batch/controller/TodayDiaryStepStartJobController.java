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
public class TodayDiaryStepStartJobController {

    private final TodayDiaryJobRunner todayDiaryJobRunner;
    private final TodayDiaryJobStepRunner todayDiaryJobStepRunner;

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

}
