package com.example.daewoo.user.service;

import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.user.dto.SocialSignupRequestDto;
import com.example.daewoo.user.dto.UserDto;
import com.example.daewoo.user.dto.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${file.upload.user.path}")
    private String userImagePath;

    @Value("${file.upload.user.webPath}")
    private String userWebPath;
    
    @Value("${project.basedir}/src/main/resources/static/userimage")
    private String staticImagePath;

    @Autowired
    private ResourceLoader resourceLoader;
    
    @PostConstruct
    public void init() {
        try {
            // Ensure the directory exists
            File uploadDir = new File(staticImagePath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
        } catch (Exception e) {
            log.error("Failed to create upload directory", e);
        }
    }

    // ======================================================================
    //  ApiUserController에서 요구하는 누락된 메서드들 
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
                updatedEntity.getImageUrl());
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
        // 1. DB에서 현재 사용자의 전체 정보를 가져옵니다.
        UserEntity existingUser = repository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다. ID: " + dto.getUserId()));

        // 2. 프론트엔드에서 받은 dto에 값이 있는 필드만 골라서 기존 정보에 덮어씁니다.
        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
            existingUser.setUsername(dto.getUsername());
        }
        if (dto.getUserAddress() != null && !dto.getUserAddress().isBlank()) {
            existingUser.setUserAddress(dto.getUserAddress());
        }
        if (dto.getUserPhone() != null && !dto.getUserPhone().isBlank()) {
            existingUser.setUserPhone(dto.getUserPhone());
        }
        if (dto.getUserBirth() != null) {
            existingUser.setUserBirth(dto.getUserBirth());
        }
        // 비밀번호 업데이트는 별도의 API를 사용하는 것이 보안상 더 좋습니다.
        // if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
        //     existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        // }

        // 3. 변경된 내용이 적용된 엔티티를 저장합니다.
        UserEntity updatedEntity = repository.save(existingUser);

        // 4. 업데이트된 최종 결과를 DTO로 변환하여 반환합니다.
        return new UserDto(updatedEntity);
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

    @Transactional(readOnly = true)
    public UserDto getUserProfile() {
        // SecurityContext에서 현재 사용자의 인증 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("인증된 사용자를 찾을 수 없습니다.");
        }
        String userEmail = authentication.getName();
        
        // 이메일을 기반으로 사용자 정보를 데이터베이스에서 조회
        UserEntity userEntity = repository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        // Entity를 Dto로 변환하여 반환 (비밀번호는 DTO 생성자에서 null 처리됨)
        return new UserDto(userEntity);
    }


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
        // 1. 기존 프로필 이미지 삭제
        deleteOldProfileImage(userId);

        // 2. 새 파일명 생성 (사용자ID_타임스탬프.확장자)
        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = originalFilename != null ? 
            originalFilename.substring(originalFilename.lastIndexOf('.')) : ".jpg";
        String newFilename = userId + "_" + System.currentTimeMillis() + fileExtension;

        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
            try {
                String oldFilename = user.getImageUrl().substring(user.getImageUrl().lastIndexOf('/') + 1);
                File oldFile = new File(staticImagePath, oldFilename);
                if (oldFile.exists()) {
                    if (!oldFile.delete()) {
                        log.warn("Failed to delete old profile image: " + oldFile.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                log.error("기존 프로필 이미지 삭제 실패: " + e.getMessage(), e);
            }
        }
    }

    public Resource loadImage(String filename) throws IOException {
        try {
            // Try to load from classpath first
            Resource resource = resourceLoader.getResource(userImagePath + filename);
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new RuntimeException("File not found " + filename);
        } catch (Exception e) {
            throw new IOException("이미지를 로드할 수 없습니다: " + filename, e);
        }
    }
}