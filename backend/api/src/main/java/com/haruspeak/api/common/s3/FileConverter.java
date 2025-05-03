package com.haruspeak.api.common.s3;

import com.haruspeak.api.common.exception.ErrorCode;
import com.haruspeak.api.common.exception.today.BlankBase64Exception;
import com.haruspeak.api.common.exception.today.InvalidBase64Exception;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.UUID;

public class FileConverter {

    /**
     * Base64 -> MultipartFile로 변환
     * @param base64String base64 문자열
     * @return 변환된 파일
     */
    public static MultipartFile fromBase64(String base64String) {
        if (base64String == null || base64String.isBlank()) {
            throw new BlankBase64Exception();
        }

        String prefix = "";
        String base64Data = base64String;

        if (base64String.contains(",")) {
            prefix = base64String.substring(0, base64String.indexOf(","));
            base64Data = base64String.substring(base64String.indexOf(",") + 1);
        }

        String contentType = "application/octet-stream";
        if (prefix.contains(":") && prefix.contains(";")) {
            contentType = prefix.substring(prefix.indexOf(":") + 1, prefix.indexOf(";"));
        }

        if (!contentType.startsWith("image/")) {
            throw new InvalidBase64Exception(); // 이미지만 허용
        }

        // 확장자 추출
        String extension = switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/jpg" -> ".jpg";
            case "image/webp" -> ".webp";
            default -> "";
        };

        String filename = UUID.randomUUID() + extension;

        try {
            byte[] decoded = Base64.getDecoder().decode(base64Data);
            return new Base64DecodedMultipartFile(decoded, filename, contentType);
        } catch (IllegalArgumentException e) {
            throw new InvalidBase64Exception();
        }
    }
}


