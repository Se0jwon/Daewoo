package com.example.daewoo.review.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.review.dto.ReviewDto;
import com.example.daewoo.review.dto.ReviewEntity;
import com.example.daewoo.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/accommodation")
public class ApiReviewController extends CommonRestController {

    @Autowired
    private ReviewService service;

    @PostMapping("/{comId}/review")
    public ResponseEntity<ResponseDto> insert(@RequestBody ReviewDto dto, @PathVariable Long comId){
        try{
            dto.setComId(comId);
            this.service.insert(dto);
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
    public ResponseEntity<ResponseDto> update(@RequestBody ReviewDto dto
            , @PathVariable Long comId
            , @PathVariable Long reviewId){
        try{
            dto.setReviewId(reviewId);
            dto.setComId(comId);
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
