package com.example.daewoo.accommodation.image.apicontroller;

import com.example.daewoo.accommodation.image.dto.ComImageDetailDto;
import com.example.daewoo.accommodation.image.dto.ComImageDto;
import com.example.daewoo.accommodation.image.service.ComImageRepository;
import com.example.daewoo.accommodation.image.service.ComImageService;
import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/accommodation/images")
public class ApiComImageController extends CommonRestController {

    @Autowired
    private ComImageService comImageService;

    @GetMapping("/load/{filename}")
    public ResponseEntity<Resource> loadImage(@PathVariable String filename) {
        try {
            // 1. 서비스에서 Resource 객체를 가져옵니다.
            Resource resource = comImageService.loadImage(filename);

            // 2. 파일의 MIME 타입을 동적으로 결정합니다.
            //    이 로직은 서비스나 유틸리티 클래스에서 처리하는 것이 더 좋습니다.
            //    여기서는 설명을 위해 직접 작성합니다.
            String contentType = null;
            try {
                contentType = Files.probeContentType(resource.getFile().toPath());
            } catch (IOException e) {
                log.error("Failed to determine content type for file: {}", filename, e);
            }
            if (contentType == null) {
                contentType = "application/octet-stream"; // 기본값
            }

            // 3. HTTP 응답 헤더에 Content-Type을 설정하여 반환합니다.
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);

        } catch (RuntimeException e) {
            log.error("Image load error for filename {}: {}", filename, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{comId}")
    public ResponseEntity<ResponseDto> findComImage(@PathVariable Long comId){
        try{
            ComImageDetailDto dto = this.comImageService.findComImage(comId);
            return getResponseEntity(ResponseCode.SUCCESS, "Image Select Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Image Select Error", null, e);
        }
    }


}
