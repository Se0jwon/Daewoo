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
@RequestMapping("api/review")
public class ApiReviewController extends CommonRestController {

    @Autowired
    private ReviewService service;

    @PostMapping("")
    public ResponseEntity<ResponseDto> insert(@RequestBody ReviewDto dto){
        try{
            service.insert(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Insert Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Insert Error", dto, e);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto> findAll(@PageableDefault(size = 5, sort = "reviewId", direction = Sort.Direction.ASC)
                                                         Pageable pageable){
        try {
            Page<ReviewDto> list = this.service.findAll(pageable);
            return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", list, null);
        }catch (Throwable e){
            log.error("예외 : "+e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find All Error", null, e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable Long id){
        try {
            Optional<ReviewDto> find = this.service.findById(id);
            return getResponseEntity(ResponseCode.SUCCESS, "Find One Ok", find, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find One Error", null, e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> update(@RequestBody ReviewDto dto,@PathVariable Long id){
        try{
            dto.setReviewId(id);
            service.update(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Update Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.UPDATE_FAIL, "Update Error", dto, e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long id){
        try{
            service.delete(id);
            return getResponseEntity(ResponseCode.SUCCESS, "Delete Ok", id, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.UPDATE_FAIL, "Delete Error", id, e);
        }
    }
}
