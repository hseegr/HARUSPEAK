package com.haruspeak.batch.common.s3;

import com.haruspeak.batch.common.exception.DiaryThumbnailException;
import com.haruspeak.batch.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file, String key) {

        try {
            Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile.toFile());

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, tempFile);
            Files.delete(tempFile);
        }catch (Exception e) {
            log.error("S3 업로드 중 오류 발생", e);
            throw new DiaryThumbnailException(ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
        }

        // S3주소
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
    }
}

