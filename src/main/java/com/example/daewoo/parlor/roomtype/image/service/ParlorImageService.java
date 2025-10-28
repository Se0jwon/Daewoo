package com.example.daewoo.parlor.roomtype.image.service;

import com.example.daewoo.accommodation.image.dto.ComImageDetailDto;
import com.example.daewoo.accommodation.image.dto.ComImageEntity;
import com.example.daewoo.parlor.roomtype.RoomTypeDto;
import com.example.daewoo.parlor.roomtype.RoomTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Service
public class ParlorImageService {
    @Autowired
    private ParlorImageRepository parlorImageRepository;

    @Value("${file.upload.parlor.path}")
    private String parlorImagePath;

    public Resource loadImage(String filename) {
        try {
            // Load image from classpath
            String classpath = "classpath:static/parlorimage/" + filename;
            Resource resource = new ClassPathResource("static/parlorimage/" + filename);
            
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

    public RoomTypeDto findById(Long roomTypeId){
        Optional<RoomTypeEntity> entities = this.parlorImageRepository.findById(roomTypeId);
        return entities.map(RoomTypeDto::fromEntity).orElse(null);
    }

}
