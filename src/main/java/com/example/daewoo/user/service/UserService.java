package com.example.daewoo.user.service;

import com.example.daewoo.user.dto.UserDto;
import com.example.daewoo.user.dto.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public UserDto insert(UserDto dto){
        // DTO를 Entity로 변환합니다. (userId는 null 상태)
        UserEntity entity = UserEntity.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .userAddress(dto.getUserAddress())
                .userPhone(dto.getUserPhone())
                .userBirth(dto.getUserBirth())
                .userEmail(dto.getEmail())
                .build();
        UserEntity savedEntity = this.repository.save(entity);

        dto.setUserId(savedEntity.getUserId());

        return dto;
    }

    // Entity 리스트를 DTO 리스트로 변환하여 반환
    public List<UserDto> findAll(){
        return this.repository.findAll().stream()
                .map(entity -> new UserDto(
                        entity.getUserId(),
                        entity.getUsername(),
                        null, // 보안을 위해 password는 반환하지 않음
                        entity.getUserAddress(),
                        entity.getUserPhone(),
                        entity.getUserEmail(),
                        entity.getUserBirth()))
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
                        entity.getUserBirth()));
    }

    // DTO를 받아 Entity를 업데이트
    public UserDto update(UserDto dto){
        UserEntity entity = this.repository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // DTO에서 받은 데이터로 Entity 필드 업데이트
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setUserAddress(dto.getUserAddress());
        entity.setUserPhone(dto.getUserPhone());
        entity.setUserBirth(dto.getUserBirth());
        entity.setUserEmail(dto.getEmail());

        UserEntity updatedEntity = this.repository.save(entity);
        return new UserDto(
                updatedEntity.getUserId(),
                updatedEntity.getUsername(),
                null, // 보안을 위해 password는 반환하지 않음
                updatedEntity.getUserAddress(),
                updatedEntity.getUserPhone(),
                updatedEntity.getUserEmail(),
                updatedEntity.getUserBirth());
    }

    // ID를 받아 삭제
    public void delete(Long id){
        this.repository.deleteById(id);
    }
}