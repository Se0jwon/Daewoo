package com.example.daewoo.user.service;

import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.review.dto.ReviewDto;
import com.example.daewoo.user.dto.UserDto;
import com.example.daewoo.user.dto.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto insert(UserDto dto){
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

        return dto;
    }

    // Entity 리스트를 DTO 리스트로 변환하여 반환
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
}