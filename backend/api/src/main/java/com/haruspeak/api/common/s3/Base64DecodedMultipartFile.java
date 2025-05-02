package com.haruspeak.api.common.s3;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class Base64DecodedMultipartFile implements MultipartFile {

    private final byte[] fileContent;
    private final String originalFileName;
    private final String contentType;

    public Base64DecodedMultipartFile(byte[] fileContent, String originalFileName, String contentType) {
        this.fileContent = fileContent;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return originalFileName;
    }

    @Override
    public String getOriginalFilename() {
        return originalFileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte[] getBytes() {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(File dest) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(fileContent);
        }
    }

}
