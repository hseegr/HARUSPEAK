package com.haruspeak.batch.listener;

import com.haruspeak.batch.service.redis.TodayDiaryRedisKeyService;
import com.haruspeak.batch.service.redis.TodayMomentRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class SummaryStepListener implements StepExecutionListener {

    private final TodayMomentRedisService todayMomentRedisService;
    private final TodayDiaryRedisKeyService todayDiaryRedisKeyService;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.debug("🐛 STEP 시작 전, 일기의 KEY 목록을 REDIS에 저장");

        String date = stepExecution.getJobParameters().getString("date");
        Set<String> keys = todayMomentRedisService.getAllKeys(date);

        if(keys == null || keys.isEmpty()) {
            return;
        }
        log.debug("🐛 배치 대상 KEY(사용자) 수: {}", keys.size());
        todayDiaryRedisKeyService.pushAllKeys(keys);
    }


    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if(stepExecution.getExitStatus().equals(ExitStatus.FAILED)) {
            log.warn("❌ STEP 실패로 today key 목록 복구");
            todayDiaryRedisKeyService.recoverProcessingKeys();
        } else {
            log.debug("🐛 STEP 종료 후, processing key 목록 초기화");
            todayDiaryRedisKeyService.clearProcessingKeys();
        }
        return stepExecution.getExitStatus();
    }
}