package com.example.daewoo.user.card.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.user.card.dto.CardDto;
import com.example.daewoo.user.card.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/card")
public class ApiCardController extends CommonRestController {
    @Autowired
    private CardService cardService;

    @PostMapping("")
    public ResponseEntity<ResponseDto> insert(@RequestBody CardDto dto){
        try{
            cardService.insert(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Card Insert Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Card Insert Error", null, e);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto> findCardList(@PathVariable Long userId){
        try{
            List<CardDto> list = cardService.findCardList(userId);
            return getResponseEntity(ResponseCode.SUCCESS, "Card Select Ok", list, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Card Select Error", null, e);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long userId){
        try{
            cardService.delete(userId);
            return getResponseEntity(ResponseCode.SUCCESS, "Card Delete Ok", userId, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Card Delete Error", null, e);
        }
    }
}
