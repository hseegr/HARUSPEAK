package com.haruspeak.batch.test;

import com.haruspeak.batch.common.client.fastapi.DailyThumbnailClient;
import com.haruspeak.batch.common.s3.S3Service;
import com.haruspeak.batch.model.TodayDiary;
import com.haruspeak.batch.model.repository.TodayDiaryRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestContoller {

    private final S3Service s3Service;
    private final DailyThumbnailClient dailyThumnailClient;

    private final TodayDiaryRedisRepository todayDiaryRedisRepository;


    @PostMapping("/test/upload")
    public String uploadFile(@RequestBody String content) {
        log.info("uploadFile: {}", content);
        String base64 = dailyThumnailClient.getDailyThumbnail(content).base64();
        log.info("Successfully got image");
        String url = s3Service.uploadImagesAndGetUrls(base64);
        log.info("Successfully uploaded image");
        return url;
    }

    @PostMapping("/test/redis/save")
    public String saveRedisDiary(@RequestBody TodayDiary diary) {
        String userId = "1";
        String date = "2025-05-12";
        todayDiaryRedisRepository.saveTodayDiaryToRedis(userId, date, diary);
        return "success";
    }

    @GetMapping("/test/redis/find")
    public TodayDiary findRedisDiary() {
        String userId = "1";
        String date = "2025-05-12";
        String key = "user:" + userId + ":" + date;
        return todayDiaryRedisRepository.getTodayDiaryByKey(key);
    }

}
