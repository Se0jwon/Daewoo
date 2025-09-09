package com.example.daewoo.review.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.review.dto.ReviewEntity;
import com.example.daewoo.review.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ResponseDto> insert(@RequestBody ReviewEntity entity){
        try{
            service.insert(entity);
            return getResponseEntity(ResponseCode.SUCCESS, "Insert Ok", entity, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Insert Error", entity, e);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto> findAll(){
        try {
            List<ReviewEntity> list = this.service.findAll();
            return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", list, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find All Error", null, e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable Long id){
        try {
            Optional<ReviewEntity> find = this.service.findById(id);
            return getResponseEntity(ResponseCode.SUCCESS, "Find One Ok", find, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find One Error", null, e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> update(@RequestBody ReviewEntity entity,@PathVariable Long id){
        try{
            id = entity.getReviewId();
            service.update(entity);
            return getResponseEntity(ResponseCode.SUCCESS, "Update Ok", entity, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.UPDATE_FAIL, "Update Error", entity, e);
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
