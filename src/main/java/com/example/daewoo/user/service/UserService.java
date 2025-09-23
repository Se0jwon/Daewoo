package com.example.daewoo.user.service;

import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.user.dto.UserDto;
import com.example.daewoo.user.dto.UserEntity;
import com.example.daewoo.user.service.UserRepository; // 이 부분을 추가해야 합니다.
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.*;
import java.util.stream.Collectors;

// ... 이하 코드는 기존과 동일 ...

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final long VERIFICATION_CODE_TTL = 300L; // 5분 (300초)

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public String generateVerificationCode(String userEmail) {
        Random random = new Random();
        String code = String.format("%0" + VERIFICATION_CODE_LENGTH + "d", random.nextInt((int) Math.pow(10, VERIFICATION_CODE_LENGTH)));
        redisTemplate.opsForValue().set(userEmail, code, Duration.ofSeconds(VERIFICATION_CODE_TTL));

        return code;
    }

    public boolean verifyCode(String userEmail, String code) {

        if (userEmail == null || userEmail.trim().isEmpty()) {
            return false;
        }
        String storedCode = redisTemplate.opsForValue().get(userEmail);

        // Redis에 저장된 코드가 존재하고, 입력된 코드와 일치하는지 확인
        if (storedCode != null && storedCode.equals(code)) {
            return true;
        }

        return false;
     }

    //사용자 이미지 업로드
    @Transactional
    public String imageUpload(Long userId, MultipartFile image) throws IOException {
        UserEntity entity = repository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("사용자가 존재하지 않습니다."));

        if(image.isEmpty()){
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        String originalFilename = image.getOriginalFilename();
        String uuidFilename = UUID.randomUUID().toString()+"_"+originalFilename;

        File file = new File(uploadDir + uuidFilename);
        image.transferTo(file);

        entity.setImageUrl(uuidFilename);
        repository.save(entity);

        return "/images/"+uuidFilename;
    }

    public Resource loadImage(String filename) {
        try {
            // Path 객체를 사용하여 파일 경로를 안전하게 조합합니다.
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                // 파일이 존재하지 않거나 읽을 수 없는 경우 예외를 발생시킵니다.
                throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다: " + filename);
            }
        } catch (MalformedURLException e) {
            // 파일 경로가 유효하지 않은 URL 형식일 때 예외를 발생시킵니다.
            throw new RuntimeException("파일 경로가 올바르지 않습니다: " + filename, e);
        }
    }

    // 회원가입 시 임시 저장 기능
    public void insert(UserDto dto) {
        if (repository.findByUserEmail(dto.getUserEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        String userJson = convertUserDtoToJson(dto);
        redisTemplate.opsForValue().set("signup:" + dto.getUserEmail(), userJson, Duration.ofMinutes(10));
    }

    // 비밀번호 재설정 인증번호 발송 요청 메서드
    public String sendPasswordResetCode(String userEmail) {
        // 이메일이 데이터베이스에 존재하는지 확인
        if (repository.findByUserEmail(userEmail).isEmpty()) {
            throw new RuntimeException("등록되지 않은 이메일입니다.");
        }
        // 인증번호 생성 및 Redis에 저장
        return generateVerificationCode(userEmail);
    }


    public UserDto saveUserToDatabase(String userEmail) {
        String userJson = redisTemplate.opsForValue().get("signup:" + userEmail);
        if (userJson == null) {
            throw new RuntimeException("회원가입 정보가 만료되었거나 존재하지 않습니다.");
        }
        UserDto dto = convertJsonToUserDto(userJson);

        UserEntity entity = UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .userAddress(dto.getUserAddress())
                .userPhone(dto.getUserPhone())
                .userBirth(dto.getUserBirth())
                .userEmail(dto.getUserEmail())
                .build();
        UserEntity savedEntity = this.repository.save(entity);
        dto.setUserId(savedEntity.getUserId());

        redisTemplate.delete("signup:" + userEmail);
        return dto;
    }

    // Entity 리스트를 DTO 리스트로 변환하여 반환
    // 비밀번호 재설정 기능
    public UserDto resetPassword(String userEmail, String newPassword) {
        UserEntity entity = this.repository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(newPassword, entity.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다.");
        }
        entity.setPassword(passwordEncoder.encode(newPassword));
        UserEntity updatedEntity = this.repository.save(entity);

        // 비밀번호 재설정 완료 후 Redis에서 인증번호 삭제
        redisTemplate.delete(userEmail);

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

    public List<UserDto> findAll(){
        return this.repository.findAll().stream()
                .map(entity -> new UserDto(
                        entity.getUserId(),
                        entity.getUsername(),
                        null,
                        entity.getUserAddress(),
                        entity.getUserPhone(),
                        entity.getUserEmail(),
                        entity.getUserBirth(),
                        entity.getImageUrl(),
                        entity.getReservations()
                                .stream()
                                .map(ReservationDto::fromEntity)
                                .toList()))
                .collect(Collectors.toList());

    }

    // Entity를 DTO로 변환하여 반환
    public Optional<UserDto> findById(Long id){
        return this.repository.findById(id)
                       .map(entity -> new UserDto(
                        entity.getUserId(),
                        entity.getUsername(),
                        null,
                        entity.getUserAddress(),
                        entity.getUserPhone(),
                        entity.getUserEmail(),
                        entity.getUserBirth(),
                        entity.getImageUrl(),
                        entity.getReservations()
                                .stream()
                                .map(ReservationDto::fromEntity)
                                .toList()));
    }

    public UserDto update(UserDto dto){
        UserEntity entity = this.repository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        entity.setUsername(dto.getUsername());
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setUserAddress(dto.getUserAddress());
        entity.setUserPhone(dto.getUserPhone());
        entity.setUserBirth(dto.getUserBirth());
        entity.setUserEmail(dto.getUserEmail());

        UserEntity updatedEntity = this.repository.save(entity);
        return new UserDto(
                updatedEntity.getUserId(),
                updatedEntity.getUsername(),
                null,
                updatedEntity.getUserAddress(),
                updatedEntity.getUserPhone(),
                updatedEntity.getUserEmail(),
                updatedEntity.getUserBirth(),
                updatedEntity.getImageUrl(),
                entity.getReservations()
                        .stream()
                        .map(ReservationDto::fromEntity)
                        .toList());
    }

    public void delete(Long id){
        this.repository.deleteById(id);
    }

    private String convertUserDtoToJson(UserDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting UserDto to JSON", e);
        }
    }

    private UserDto convertJsonToUserDto(String json) {
        try {
            return objectMapper.readValue(json, UserDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to UserDto", e);
        }

    }
}