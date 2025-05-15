package com.haruspeak.batch.test;

import com.haruspeak.batch.common.client.fastapi.DailyThumbnailClient;
import com.haruspeak.batch.common.s3.S3Service;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.TagRepository;
import com.haruspeak.batch.service.redis.TodayDiaryRedisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "TEST")
public class TestContoller {

    private final S3Service s3Service;
    private final DailyThumbnailClient dailyThumnailClient;

    private final TodayDiaryRedisService todayDiaryRedisService;
    private final TestRepository testRepository;
    private final TagRepository apiTagRepository;


    @PostMapping("/test/upload")
    public String uploadFile(@RequestBody String content) {
        log.info("uploadFile: {}", content);
        String base64 = dailyThumnailClient.getDailyThumbnail(content).base64();
        log.info("Successfully got image");
        String url = s3Service.uploadImagesAndGetUrls(base64);
        log.info("Successfully uploaded image");
        return url;
    }

    @PostMapping("/test/db/connection")
    public String saveToBatchTest(@RequestBody List<String> contents) {
        testRepository.bulkInsertBatchTest(contents);
        return "success";
    }

    @PostMapping("/test/db/connection/apidb")
    public String saveToTags(@RequestBody List<String> tags) {
        apiTagRepository.bulkInsertTags(tags);
        return "success";
    }

}
