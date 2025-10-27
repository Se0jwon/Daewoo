package com.example.daewoo.user.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.user.service.UserImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/images")
public class UserImageController extends CommonRestController {

    private final UserImageService userImageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String filename = userImageService.uploadImage(file);
            return getResponseEntity(ResponseCode.SUCCESS, "이미지 업로드 성공", filename, null);
        } catch (IOException e) {
            log.error("이미지 업로드 실패", e);
            return getResponseEntity(ResponseCode.UPDATE_FAIL, "이미지 업로드 실패: " + e.getMessage(), null, e);
        }
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Resource resource = userImageService.loadImage(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } catch (Exception e) {
            log.error("이미지 로드 실패: {}", filename, e);
            return ResponseEntity.notFound().build();
        }
    }
}
