package com.haruspeak.batch.service;

import com.haruspeak.batch.common.client.fastapi.DailyThumbnailClient;
import com.haruspeak.batch.common.s3.S3Service;
import com.haruspeak.batch.dto.ThumbnailProcessingResult;
import com.haruspeak.batch.dto.ThumbnailUpdateContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThumbnailService {

    private final DailyThumbnailClient dailyThumbnailClient;
    private final S3Service s3Service;

    public String generateAndUploadThumbnail(String content){
        log.debug("🐛 오늘의 썸네일 생성 및 S3 저장 후 S3 URL 요청");
        try {
            String base64 = generateThumbnailBase64(content);
            return uploadThumbnailAndGetS3Url(base64);
        }catch (Exception e) {
            log.error("💥 오늘의 썸네일 생성 및 S3 저장 중 오류 발생", e);
            throw e;
        }
        
    }

    public ThumbnailProcessingResult generateThumbnailUrlInParallel(List<ThumbnailUpdateContext> contexts){
        log.debug("🐛 썸네일 생성 + S3 저장 병렬 처리 시작 (총 {}건)", contexts.size());

        List<ThumbnailUpdateContext> successList = Collections.synchronizedList(new ArrayList<>());
        List<ThumbnailUpdateContext> failedList = Collections.synchronizedList(new ArrayList<>());

        contexts.parallelStream().forEach(context -> {
            try {
                String s3Url = generateAndUploadThumbnail(context.getContent());
                successList.add(
                        context.toBuilder()
                        .imageUrl(s3Url)
                        .content(null)
                        .build()
                );
            } catch (Exception e) {
                log.warn("⚠️ 썸네일 처리 실패 - userId: {}, date: {}", context.getUserId(), context.getWriteDate(), e);
                failedList.add(context);
            }
        });

        return new ThumbnailProcessingResult(successList, failedList);
    }

    private String generateThumbnailBase64(String totalTodayContent) {
        try {
            return dailyThumbnailClient.getDailyThumbnail(totalTodayContent).base64();
        }catch (Exception e) {
            log.error("💥 오늘의 썸네일 생성 요청 중 오류 발생", e);
            throw e;
        }
    }

    private String uploadThumbnailAndGetS3Url(String base64){
        try {
            return s3Service.uploadImagesAndGetUrls(base64);
        }catch (Exception e) {
            log.error("💥 오늘의 썸네일 S3 저장 중 오류 발생", e);
            throw e;
        }
    }
}
