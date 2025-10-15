package com.example.daewoo.user.card.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.user.card.dto.CardDto;
import com.example.daewoo.user.card.service.CardService;
import com.example.daewoo.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/card")
public class ApiCardController extends CommonRestController {
    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<ResponseDto> insert(@RequestBody CardDto dto,
                                              Authentication authentication){
        try{
            Long userId = userService.findByEmail(authentication.getName()).getUserId();
            dto.setUserId(userId);
            cardService.insert(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Card Insert Ok", dto, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Card Insert Error", null, e);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto> findCardList(Authentication authentication){
        try{
            Long userId = userService.findByEmail(authentication.getName()).getUserId();
            List<CardDto> list = cardService.findCardList(userId);
            return getResponseEntity(ResponseCode.SUCCESS, "Card Select Ok", list, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Card Select Error", null, e);
        }
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long cardId, Authentication authentication){
        try{
            Long userId = userService.findByEmail(authentication.getName()).getUserId();
            cardService.delete(cardId, userId);
            return getResponseEntity(ResponseCode.SUCCESS, "Card Delete Ok", cardId, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.DELETE_FAIL, "Card Delete Error", null, e);
        }
    }
}
