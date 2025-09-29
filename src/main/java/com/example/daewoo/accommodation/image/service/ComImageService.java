package com.example.daewoo.accommodation.image.service;

import com.example.daewoo.accommodation.dto.AccommodationEntity;
import com.example.daewoo.accommodation.dto.AccommodationOneDto;
import com.example.daewoo.accommodation.image.dto.ComImageDetailDto;
import com.example.daewoo.accommodation.image.dto.ComImageDto;
import com.example.daewoo.accommodation.image.dto.ComImageEntity;
import com.example.daewoo.accommodation.service.AccommodationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComImageService {
    @Autowired
    private ComImageRepository comImageRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Resource loadImage(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                // 파일이 존재하지 않거나 읽을 수 없는 경우 예외를 발생시킵니다.
                throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다: " + filename);
            }
        } catch (MalformedURLException e) {
            // 파일 경로가 유효하지 않은 URL 형식일 때 예외를 발생시킵니다.
            throw new RuntimeException("파일 경로가 올바르지 않습니다: " + filename, e);
        }
    }

    public String getMimeType(Resource resource) throws IOException {
        String contentType = Files.probeContentType(resource.getFile().toPath());
        return contentType != null ? contentType : "application/octet-stream";
    }

    public ComImageDetailDto findComImage(Long comId){
        List<ComImageEntity> entities = this.comImageRepository.findByAccommodation_ComId(comId);

        String mainImage = entities.stream()
                .filter(ComImageEntity::getIsMain)
                .map(ComImageEntity::getImageUrl)
                .findFirst()
                .orElse(null);

        List<String> subImages = entities.stream()
                .filter(image -> !image.getIsMain())
                .map(ComImageEntity::getImageUrl)
                .toList();

        return new ComImageDetailDto(comId, mainImage, subImages);
    }

}
