package com.example.daewoo.user.service;

import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.review.dto.ReviewDto;
import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.user.dto.UserDto;
import com.example.daewoo.user.dto.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
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

    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final long VERIFICATION_CODE_TTL = 300L; // 5분 (300초)

    public String generateVerificationCode(String userEmail) {
        Random random = new Random();
        String code = String.format("%0" + VERIFICATION_CODE_LENGTH + "d", random.nextInt((int) Math.pow(10, VERIFICATION_CODE_LENGTH)));

        // Redis에 이메일 주소를 키로, 인증번호를 값으로 저장하고 5분 뒤 만료되도록 설정
        redisTemplate.opsForValue().set(userEmail, code, Duration.ofSeconds(VERIFICATION_CODE_TTL));

        return code;
    }

    public boolean verifyCode(String userEmail, String code) {
        String storedCode = redisTemplate.opsForValue().get(userEmail); // Redis에서 인증번호 조회

        // 인증번호가 존재하고, 사용자가 입력한 코드와 일치하는지 확인
        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(userEmail); // 인증 성공 시 Redis 데이터 삭제
            return true;
        }
        return false;
    }

    // 회원가입 시 임시 저장 기능
    public void insert(UserDto dto) {
        if(dto.getReservations() == null){
            dto.setReservations(null);
        }
        String userJson = convertUserDtoToJson(dto);
        redisTemplate.opsForValue().set("signup:" + dto.getUserEmail(), userJson, Duration.ofMinutes(10)); // 10분 동안 임시 저장
    }

    // 이메일 인증 후 실제 데이터베이스에 저장
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
        redisTemplate.delete("signup:" + userEmail); // 저장 완료 후 Redis 데이터 삭제
        return dto;
    }

    // Entity 리스트를 DTO 리스트로 변환하여 반환
    // 비밀번호 재설정 기능
    public UserDto resetPassword(String userEmail, String newPassword) {
        UserEntity entity = this.repository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        entity.setPassword(passwordEncoder.encode(newPassword));
        UserEntity updatedEntity = this.repository.save(entity);
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

    // --- 기존의 다른 메서드들 (findAll, findById, update, delete)은 그대로 유지됩니다. ---
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

    // JSON 직렬화/역직렬화 Helper 메서드
    private String convertUserDtoToJson(UserDto dto) {
        // 실제로는 ObjectMapper를 사용해야 하지만, 여기서는 간단히 표현
        return String.format("{\"username\":\"%s\",\"password\":\"%s\",\"userEmail\":\"%s\"}",
                dto.getUsername(), dto.getPassword(), dto.getUserEmail());
    }

    private UserDto convertJsonToUserDto(String json) {
        // 실제로는 ObjectMapper를 사용해야 하지만, 여기서는 간단히 표현
        String username = json.split("\"username\":\"")[1].split("\"")[0];
        String password = json.split("\"password\":\"")[1].split("\"")[0];
        String userEmail = json.split("\"userEmail\":\"")[1].split("\"")[0];
        UserDto dto = new UserDto();
        dto.setUsername(username);
        dto.setPassword(password);
        dto.setUserEmail(userEmail);
        return dto;
    }
}