package com.example.daewoo.user.apicontroller;

import com.example.daewoo.common.CommonRestController;
import com.example.daewoo.common.ResponseCode;
import com.example.daewoo.common.ResponseDto;
import com.example.daewoo.common.jwt.JwtTokenProvider;
import com.example.daewoo.user.dto.UserDto;
import com.example.daewoo.user.dto.LoginDto;
import com.example.daewoo.user.dto.EmailVerificationDto;
import com.example.daewoo.user.dto.PasswordResetDto;
import com.example.daewoo.user.service.EmailService;
import com.example.daewoo.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
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

    @Autowired
    private EmailService emailService;

    // 회원가입 요청 (1단계): 이메일 인증번호 전송 및 임시 저장
    @PostMapping("")
    public ResponseEntity<ResponseDto> registerAndSendVerificationEmail(@RequestBody UserDto dto){
        try{
            service.insert(dto); // Redis에 사용자 정보 임시 저장
            String verificationCode = service.generateVerificationCode(dto.getUserEmail());
            emailService.sendVerificationCode(dto.getUserEmail(), verificationCode);
            return getResponseEntity(ResponseCode.SUCCESS, "회원가입을 위해 이메일로 전송된 인증번호를 확인해주세요.", null, null);
        }catch (Throwable e){
            log.error(e.toString());
            return getResponseEntity(ResponseCode.INSERT_FAIL, "회원가입 실패", dto, e);
        }
    }

    // 이메일 인증번호 확인 및 회원가입 최종 완료 (2단계)
    @PostMapping("/verify-email")
    public ResponseEntity<ResponseDto> verifyEmailAndCompleteRegistration(@RequestBody EmailVerificationDto dto) {
        try {
            if (service.verifyCode(dto.getUserEmail(), dto.getVerificationCode())) {
                UserDto result = service.saveUserToDatabase(dto.getUserEmail());
                return getResponseEntity(ResponseCode.SUCCESS, "이메일 인증 및 회원가입이 완료되었습니다.", result, null);
            } else {
                return getResponseEntity(ResponseCode.INVALID_REQUEST, "유효하지 않은 인증번호입니다.", null, null);
            }
        } catch (Throwable e) {
            log.error(e.toString());
            return getResponseEntity(ResponseCode.ERROR, "이메일 인증 실패", null, e);
        }
    }

    // 비밀번호 재설정 API
    @PatchMapping("/reset-password")
    public ResponseEntity<ResponseDto> resetPassword(@RequestBody PasswordResetDto dto) {
        try {
            if (service.verifyCode(dto.getUserEmail(), dto.getVerificationCode())) {
                service.resetPassword(dto.getUserEmail(), dto.getNewPassword());
                return getResponseEntity(ResponseCode.SUCCESS, "비밀번호가 성공적으로 재설정되었습니다.", null, null);
            } else {
                return getResponseEntity(ResponseCode.INVALID_REQUEST, "유효하지 않은 인증번호입니다.", null, null);
            }
        } catch (Throwable e) {
            log.error(e.toString());
            return getResponseEntity(ResponseCode.ERROR, "비밀번호 재설정 실패", null, e);
        }
    }

    // --- 기존의 다른 메서드들 (login, findAll, findById, update, delete)은 그대로 유지됩니다. ---

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

    // 이미지 업로드
    @PostMapping("/{userId}/profile-image")
    public ResponseEntity<ResponseDto> updateUserProfileImage(
            @PathVariable Long userId,
            @RequestParam("image") MultipartFile imageFile) {
        try {
            // 서비스를 호출하여 이미지 업로드 및 URL 업데이트 처리
            String imageUrl = service.imageUpload(userId, imageFile);
            return getResponseEntity(ResponseCode.SUCCESS, "Image Upload Ok", imageUrl, null);
        } catch (Throwable e) {
            log.error(e.toString());
            return getResponseEntity(ResponseCode.UPDATE_FAIL, "Image Upload Error", null, e);
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<ResponseDto> serveImage(@PathVariable String filename) {
        try {
            // 1. 파일 경로 생성
            Resource resource = service.loadImage(filename);
            return getResponseEntity(ResponseCode.SUCCESS, "Image Load Ok", resource, null);
        } catch (Throwable e) {
            // 5. 경로가 이상하면 500 에러 반환
            return getResponseEntity(ResponseCode.SELECT_FAIL, "Image Load Error", null, e);
        }
    }
}