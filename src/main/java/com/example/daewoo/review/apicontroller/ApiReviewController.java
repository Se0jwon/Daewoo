package com.example.daewoo.review.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.review.dto.ReviewDto;
import com.example.daewoo.review.dto.ReviewEntity;
import com.example.daewoo.review.service.ReviewService;
import com.example.daewoo.user.service.UserService; // [✅ 1. UserService 임포트]
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // [✅ 2. Authentication 임포트]
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/accommodation")
public class ApiReviewController extends CommonRestController {

    @Autowired
    private ReviewService service;

    @Autowired
    private UserService userService; // [✅ 3. UserService 주입]

    @PostMapping("/{comId}/review")
    public ResponseEntity<ResponseDto> insert(@RequestBody ReviewDto dto,
                                              @PathVariable Long comId,
                                              Authentication authentication){ // [✅ 4. Authentication 매개변수 추가]
        try{
            // [✅ 5. 토큰에서 실제 userId 추출]
            Long userId = userService.findByEmail(authentication.getName()).getUserId();

            dto.setComId(comId);

            // [✅ 6. 서비스 호출 시 userId 전달]
            this.service.insert(userId, dto);

            return getResponseEntity(ResponseCode.SUCCESS, "Insert Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Insert Error", dto, e);
        }
    }


    @GetMapping("/{comId}/review")
    public ResponseEntity<ResponseDto> findReview(@PathVariable Long comId,
                                                  @PageableDefault(size = 5, sort = "reviewId", direction = Sort.Direction.DESC)
                                                  Pageable pageable){
        try {
            Page<ReviewDto> list = this.service.findAllByAccommodationEntity_ComId(comId, pageable);
            return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", list, null);
        }catch (Throwable e){
            log.error("예외 : "+e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find All Error", null, e);
        }
    }

    @PatchMapping("/{comId}/review/{reviewId}")
    public ResponseEntity<ResponseDto> update(@RequestBody ReviewDto dto,
                                              @PathVariable Long comId,
                                              @PathVariable Long reviewId,
                                              Authentication authentication){ // [✅ 7. (권장) Update에도 동일하게 적용]
        try{
            // [✅ 8. (권장) 토큰에서 userId 추출]
            Long userId = userService.findByEmail(authentication.getName()).getUserId();

            dto.setReviewId(reviewId);
            dto.setComId(comId);
            dto.setUserId(userId); // ReviewService.update가 DTO의 userId를 사용하므로 설정

            service.update(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Update Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.UPDATE_FAIL, "Update Error", dto, e);
        }
    }

    @DeleteMapping("review/{reviewId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long reviewId){
        try{
            service.delete(reviewId);
            return getResponseEntity(ResponseCode.SUCCESS, "Delete Ok", reviewId, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.UPDATE_FAIL, "Delete Error", reviewId, e);
        }
    }
}