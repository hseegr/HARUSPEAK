package com.haruspeak.api.common.s3;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.HaruspeakException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Uploader s3Uploader;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 이미지 업로드 및 주소 반환
     * @param images
     * @return
     */
    public String uploadImagesAndGetUrls(String images){
        MultipartFile file = FileConverter.fromBase64(images);
        String key = "uploads/" + file.getOriginalFilename();
        return s3Uploader.uploadFile(file, key);
    }

    /**
     * S3에서 이미지 삭제
     * @param imageUrl
     */
    public void deleteImages(String imageUrl){
        try {
            String splitStr = ".com/";
            String key = imageUrl.substring(imageUrl.lastIndexOf(splitStr) + splitStr.length());

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);

        } catch (Exception e) {
            throw new HaruspeakException(ErrorCode.IMAGE_DELETE_ERROR);
        }
    }
}
