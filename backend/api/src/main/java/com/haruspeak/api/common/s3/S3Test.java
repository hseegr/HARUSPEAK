package com.haruspeak.api.common.s3;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class S3Test {

    private final S3Uploader s3Uploader;


//    @Hidden
    @PostMapping("/upload-base64")
    public ResponseEntity<String> uploadBase64(@RequestBody Base64UploadRequest request) {
        MultipartFile file = FileConverter.fromBase64(request.base64());
        String key = "uploads/" + file.getOriginalFilename();
        String url = s3Uploader.uploadFile(file, key);
        return ResponseEntity.ok(url);
    }

    public record Base64UploadRequest(String base64) {}


}
