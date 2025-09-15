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

    // DTO를 받아 Entity로 변환 후 저장
    public UserDto insert(UserDto dto){
        UserEntity entity = UserEntity.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build();
        UserEntity savedEntity = this.repository.save(entity);
        dto.setUserId(savedEntity.getUserId());
        return dto;
    }

    // Entity 리스트를 DTO 리스트로 변환하여 반환
    public List<UserDto> findAll(){
        return this.repository.findAll().stream()
                .map(entity -> new UserDto(entity.getUserId(), entity.getUsername(), entity.getEmail()))
                .collect(Collectors.toList());
    }

    // Entity를 DTO로 변환하여 반환
    public Optional<UserDto> findById(Long id){
        return this.repository.findById(id)
                .map(entity -> new UserDto(entity.getUserId(), entity.getUsername(), entity.getEmail()));
    }

    // DTO를 받아 Entity를 업데이트
    public UserDto update(UserDto dto){
        UserEntity entity = this.repository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        UserEntity updatedEntity = this.repository.save(entity);
        return new UserDto(updatedEntity.getUserId(), updatedEntity.getUsername(), updatedEntity.getEmail());
    }

    // ID를 받아 삭제
    public void delete(Long id){
        this.repository.deleteById(id);
    }
}