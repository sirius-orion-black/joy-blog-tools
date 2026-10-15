package com.joy.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Base64MultipartFileUtil implements MultipartFile {
    private final byte[] content;
    private final String filename;

    public Base64MultipartFileUtil(String base64, String filename) {
        // 去除可能存在的 Data URI 前缀 (如 "data:image/png;base64,")
        String pureBase64 = base64;
        if (base64 != null && base64.contains(",")) {
            pureBase64 = base64.substring(base64.indexOf(",") + 1);
        }

        this.content = Base64.getDecoder().decode(pureBase64);
        this.filename = filename;
    }

    @Override
    public String getName() { return filename; }

    @Override
    public String getOriginalFilename() { return filename; }

    @Override
    public String getContentType() { return "image/png"; }

    @Override
    public boolean isEmpty() { return content == null || content.length == 0; }

    @Override
    public long getSize() { return content.length; }

    @Override
    public byte[] getBytes() { return content; }

    @Override
    public InputStream getInputStream() { return new ByteArrayInputStream(content); }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("请使用 getInputStream() 或 getBytes()");
    }
}
