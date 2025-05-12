package com.haruspeak.batch.test;

import com.haruspeak.batch.common.client.fastapi.DailyThumbnailClient;
import com.haruspeak.batch.common.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestContoller {

    private final S3Service s3Service;
    private final DailyThumbnailClient dailyThumnailClient;


    @PostMapping("/test/upload")
    public String uploadFile(@RequestBody String content) {
        log.info("uploadFile: {}", content);
        String base64 = dailyThumnailClient.getDailyThumbnail(content).base64();
        log.info("Successfully got image");
        String url = s3Service.uploadImagesAndGetUrls(base64);
        log.info("Successfully uploaded image");
        return url;
    }


}
