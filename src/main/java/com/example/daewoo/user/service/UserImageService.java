package com.example.daewoo.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UserImageService {
    
    @Value("${file.upload.user.path}")
    private String uploadPath;

    public String uploadImage(MultipartFile file) throws IOException {
        // Get the project root directory
        String projectRoot = System.getProperty("user.dir");
        // Create upload directory in src/main/resources/static/userimage
        File uploadDir = new File(projectRoot + "/src/main/resources/static/userimage");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String newFilename = UUID.randomUUID().toString() + fileExtension;

        // Save file
        Path filePath = Paths.get(uploadDir.getAbsolutePath(), newFilename);
        file.transferTo(filePath);

        return newFilename;
    }

    public Resource loadImage(String filename) {
        try {
            // First try to load from the source directory for development
            String projectRoot = System.getProperty("user.dir");
            File file = new File(projectRoot + "/src/main/resources/static/userimage/" + filename);
            
            if (file.exists()) {
                return new org.springframework.core.io.UrlResource(file.toURI());
            }
            
            // If not found in source directory, try the classpath (for production)
            Resource resource = new ClassPathResource("static/userimage/" + filename);
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("이미지 로드 중 오류가 발생했습니다: " + filename, e);
        }
    }

    public String getMimeType(Resource resource) throws IOException {
        String contentType = Files.probeContentType(resource.getFile().toPath());
        return contentType != null ? contentType : "application/octet-stream";
    }
}
