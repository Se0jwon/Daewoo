package com.example.daewoo.user.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.common.jwt.JwtTokenProvider;
import com.example.daewoo.user.dto.UserDto;
import com.example.daewoo.user.dto.LoginDto; // LoginDto import 추가
import com.example.daewoo.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class ApiUserController extends CommonRestController {

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUserEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenProvider.generateToken(authentication);
            return getResponseEntity(ResponseCode.SUCCESS, "Login Ok", token, null);
        } catch (Throwable e) {
            log.error(e.toString());
            return getResponseEntity(ResponseCode.LOGIN_FAIL, "Login Error", null, e);
        }
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto> insert(@RequestBody UserDto dto){
        try{
            UserDto result = service.insert(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Insert Ok", result, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "Insert Error", dto, e);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto> findAll(){
        try {
            List<UserDto> list = this.service.findAll();
            return getResponseEntity(ResponseCode.SUCCESS, "Find All Ok", list, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find All Error", null, e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable Long id){
        try {
            Optional<UserDto> find = this.service.findById(id);
            return getResponseEntity(ResponseCode.SUCCESS, "Find One Ok", find, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Find One Error", null, e);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto> update(@RequestBody UserDto dto,@PathVariable Long id){
        try{
            dto.setUserId(id);
            UserDto result = service.update(dto);
            return getResponseEntity(ResponseCode.SUCCESS, "Update Ok", result, null);
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