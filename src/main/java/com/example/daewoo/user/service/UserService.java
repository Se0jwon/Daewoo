package com.example.daewoo.user.service;

import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.user.dto.SocialSignupRequestDto;
import com.example.daewoo.user.dto.UserDto;
import com.example.daewoo.user.dto.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // ======================================================================
    // 🚨 ApiUserController에서 요구하는 누락된 메서드들 🚨
    // ======================================================================

    /**
     * 회원가입 요청 (1단계): 사용자 임시 저장
     */
    @Transactional
    public void insert(UserDto dto) {
        // 비밀번호 암호화 및 ROLE_PENDING_VERIFICATION (인증 대기) 역할 부여 후 임시 저장
        UserEntity entity = dto.toEntity();
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRole("ROLE_PENDING_VERIFICATION");
        repository.save(entity);
        // Note: 실제 구현에서는 이메일 인증이 완료되어야 저장하는 로직이 더 안전할 수 있음
    }

    /**
     * 이메일 인증번호 생성 및 Redis 저장
     */
    public String generateVerificationCode(String userEmail) {
        String code = String.format("%06d", new Random().nextInt(1000000));
        // Redis에 5분(예시) 유효기간으로 저장
        redisTemplate.opsForValue().set("VERIFY:" + userEmail, code, Duration.ofMinutes(5));
        return code;
    }

    /**
     * 이메일 인증번호 확인
     */
    public boolean verifyCode(String userEmail, String verificationCode) {
        String storedCode = redisTemplate.opsForValue().get("VERIFY:" + userEmail);
        // 코드가 일치하면 Redis에서 해당 키를 삭제하고 true 반환
        if (verificationCode != null && verificationCode.equals(storedCode)) {
            return true;
        }
        return false;
     }

    @Transactional
    public UserDto saveUserToDatabase(String userEmail) {
        UserEntity entity = repository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("인증 대기 사용자를 찾을 수 없습니다."));

        // 권한을 최종 사용자 역할로 업데이트
        entity.setRole("ROLE_USER");
        UserEntity savedEntity = repository.save(entity);
        return UserDto.fromEntity(savedEntity);
    }

    /**
     * 비밀번호 재설정 인증번호 전송
     */
    // 비밀번호 재설정 인증번호 발송 요청 메서드
    public String sendPasswordResetCode(String userEmail) {
        // 이메일이 데이터베이스에 존재하는지 확인
        if (repository.findByUserEmail(userEmail).isEmpty()) {
            throw new RuntimeException("등록되지 않은 이메일입니다.");
        }
        // 인증번호 생성 및 Redis에 저장
        return generateVerificationCode(userEmail);
    }


    /**
     * 비밀번호 재설정
     */
    public UserDto resetPassword(String userEmail, String newPassword) {
        UserEntity entity = this.repository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(newPassword, entity.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다.");
        }
        entity.setPassword(passwordEncoder.encode(newPassword));
        UserEntity updatedEntity = this.repository.save(entity);
        // 비밀번호 재설정 완료 후 Redis에서 인증번호 삭제
        redisTemplate.delete("VERIFY:" + userEmail);

        return new UserDto(
                updatedEntity.getUserId(),
                updatedEntity.getUsername(),
                null,
                updatedEntity.getUserAddress(),
                updatedEntity.getUserPhone(),
                updatedEntity.getUserEmail(),
                updatedEntity.getUserBirth(),
                updatedEntity.getImageUrl(),
                updatedEntity.getReservations()
                        .stream()
                        .map(ReservationDto::fromEntity)
                        .toList());
    }

    // ======================================================================
    // CRUD 및 이미지 관련 메서드
    // ======================================================================

    /**
     * 전체 사용자 조회 (findAll)
     */
    public List<UserDto> findAll() {
        return repository.findAll().stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }


    /**
     * ID로 사용자 조회 (findById)
     */
    public Optional<UserDto> findById(Long id) {
        return repository.findById(id).map(UserDto::fromEntity);
    }

    /**
     * 사용자 정보 업데이트 (update)
     */
    @Transactional
    public UserDto update(UserDto dto) {
        UserEntity entity = repository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // DTO의 정보로 엔티티 업데이트 (비밀번호 제외)
        entity.setUsername(dto.getUsername());
        entity.setUserAddress(dto.getUserAddress());
        entity.setUserPhone(dto.getUserPhone());

        // 필요하다면 비밀번호 변경 로직 추가

        UserEntity updatedEntity = repository.save(entity);
        return UserDto.fromEntity(updatedEntity);
    }

    /**
     * 사용자 삭제 (delete)
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // ======================================================================
    // 소셜 로그인 완료 로직 (이전에 수정한 핵심 로직)
    // ======================================================================

    // 이메일로 사용자 찾기 (JWT 인증 등에 사용)
    public UserEntity findByEmail(String email) {
        return repository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }


    /**
     * 소셜 회원가입 추가 정보 입력 및 완료 처리
     */
    @Transactional
    public UserDto completeSocialSignup(SocialSignupRequestDto dto) {
        // 1. 해당 유저 엔티티를 찾습니다.

        UserEntity entity = repository.findByOauthIdAndRegistrationId(dto.getOauthId(), dto.getRegistrationId())
                .orElseThrow(() -> new RuntimeException("소셜 로그인 정보를 찾을 수 없습니다."));

        // 2. 추가 정보를 업데이트합니다.
        entity.setUsername(dto.getUsername());
        entity.setUserAddress(dto.getUserAddress());
        entity.setUserPhone(dto.getUserPhone());
        entity.setUserBirth(dto.getUserBirth());
        entity.setPassword(passwordEncoder.encode("TEMP_OAUTH_PASSWORD"));

        // 4. 권한을 ROLE_USER로 변경합니다. (가입 완료)
        entity.setRole("ROLE_USER");

        // 5. DB에 저장합니다.
        UserEntity updatedEntity = this.repository.save(entity);

        // 6. UserDto로 변환하여 반환
        return UserDto.fromEntity(updatedEntity);
    }

    // 이미지 업로드 로직
    @Transactional
    public String imageUpload(Long userId, MultipartFile imageFile) throws IOException {
        String filename = userId + "_" + imageFile.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);

        if (!java.nio.file.Files.exists(uploadPath)) {
            java.nio.file.Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        imageFile.transferTo(filePath.toFile());

        UserEntity userEntity = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setImageUrl("/images/" + filename);
        repository.save(userEntity);

        return userEntity.getImageUrl();
    }

    public Resource loadImage(String filename) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("File not found " + filename);
        }

    }
}