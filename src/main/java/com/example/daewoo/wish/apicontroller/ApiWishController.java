package com.example.daewoo.wish.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.wish.dto.WishDto;
import com.example.daewoo.wish.service.WishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/wish")
public class ApiWishController extends CommonRestController {

    @Autowired
    private WishService service;

    @PostMapping("")
    public ResponseEntity<ResponseDto> insert(@RequestBody WishDto dto) {
        try {
            WishDto result = service.insert(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Insert Ok", result, null);
        } catch (Throwable e) {
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Insert Error", dto, e);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto> findAll() {
        try {
            List<WishDto> list = this.service.findAll();
            return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", list, null);
        } catch (Throwable e) {
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find All Error", null, e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable Long id) {
        try {
            Optional<WishDto> find = this.service.findById(id);
            return getResponseEntity(ResponseCode.SUCCESS, "Find One Ok", find, null);
        } catch (Throwable e) {
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find One Error", null, e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> update(@RequestBody WishDto dto, @PathVariable Long id) {
        try {
            dto.setWishId(id);
            WishDto result = service.update(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Update Ok", result, null);
        } catch (Throwable e) {
            log.error(e.toString());
            return getResponseEntity(ResponseCode.UPDATE_FAIL, "Update Error", dto, e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return getResponseEntity(ResponseCode.SUCCESS, "Delete Ok", id, null);
        } catch (Throwable e) {
            log.error(e.toString());
            return getResponseEntity(ResponseCode.UPDATE_FAIL, "Delete Error", id, e);
        }
    }
}