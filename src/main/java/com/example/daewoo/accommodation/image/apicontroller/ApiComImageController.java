package com.example.daewoo.accommodation.image.apicontroller;

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

import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/accommodation/images")
public class ApiComImageController extends CommonRestController {

    @Autowired
    private ComImageService comImageService;

    @GetMapping("/main/{filename}")
    public ResponseEntity<Resource> mainImage(@PathVariable String filename) {
        try {
            // 1. 파일 경로 생성
            Resource resource = comImageService.loadImage(filename);
            String contentType = "image/jpeg"; // 또는 적절한 MIME 타입 설정 로직 추가

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);
//            return getResponseEntity(ResponseCode.SUCCESS, "Image Load Ok", resource, null);
        } catch (Throwable e) {
            // 5. 경로가 이상하면 500 에러 반환
//            return getResponseEntity(ResponseCode.SELECT_FAIL, "Image Load Error", null, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{comId}")
    public ResponseEntity<ResponseDto> findComImage(@PathVariable Long comId){
        try{
            List<ComImageDto> list = this.comImageService.findComImage(comId);
            return getResponseEntity(ResponseCode.SUCCESS, "Image Select Ok", list, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Image Select Error", null, e);
        }
    }


}
