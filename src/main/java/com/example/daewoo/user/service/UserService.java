package com.example.daewoo.user.service;

import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.user.dto.UserDto;
import com.example.daewoo.user.dto.UserEntity;
import com.example.daewoo.user.service.UserRepository; // 이 부분을 추가해야 합니다.
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
                        entity.getReservations()
                                .stream()
                                .map(ReservationDto::fromEntity)
                                .toList()))
                .collect(Collectors.toList());
    }

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