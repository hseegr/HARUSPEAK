package com.haruspeak.batch.common.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Uploader s3Uploader;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * base64로 받은 이미지 파일 변환 후 S3에 이미지 업로드 및 주소 반환
     * @param image
     * @return
     */
    public String uploadImageAndGetUrl(String image){
        MultipartFile file = FileConverter.fromBase64("data:image/png;base64," + image);
        String key = "uploads/" + file.getOriginalFilename();
        return s3Uploader.uploadFile(file, key);
    }

    /**
     * S3에 이미지 업로드 및 주소 반환
     * @param image
     * @return
     */
    public String uploadImageDataAndGetUrl(String image){
        MultipartFile file = FileConverter.fromBase64(image);
        String key = "uploads/" + file.getOriginalFilename();
        return s3Uploader.uploadFile(file, key);
    }
}

