package com.example.daewoo.wish.service;

import com.example.daewoo.accommodation.dto.AccommodationEntity;
import com.example.daewoo.accommodation.service.AccommodationRepository;
import com.example.daewoo.parlor.dto.ParlorEntity;
import com.example.daewoo.parlor.service.ParlorRepository;
import com.example.daewoo.user.dto.UserEntity;
import com.example.daewoo.user.service.UserRepository;
import com.example.daewoo.wish.dto.WishDto;
import com.example.daewoo.wish.dto.WishEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WishService {
    @Autowired
    private WishRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    public WishDto insert(Long userId, WishDto dto) {
        WishEntity entity = dto.toEntity();

        AccommodationEntity accommodationEntity = accommodationRepository.findById(dto.getComId())
                .orElseThrow(() -> new RuntimeException("Parlor Not Found"));
        entity.setAccommodationEntity(accommodationEntity);

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setUserEntity(userEntity);

        this.repository.save(entity);

//        WishEntity entity = WishEntity.builder()
//                .userEntity(dto.getUserId())
//                .parlorId(dto.getParlorId())
//                .build();
//        WishEntity savedEntity = this.repository.save(entity);
//        dto.setWishId(savedEntity.getWishId());
        return dto;
    }

    public List<WishDto> findAll(Long userId) {

        return this.repository.findByUserEntity_UserId(userId).stream()
                .map(WishDto::fromEntity)
                .collect(Collectors.toList());
    }

//    public Optional<WishDto> findById(Long id) {
//        return this.repository.findById(id)
//                .map(entity -> new WishDto(entity.getWishId(), entity.getUserEntity(), entity.getParlorEntity()));
//    }

//    public WishDto update(Long userId, WishDto dto) {
//        WishEntity entity = dto.toEntity();
//
//        ParlorEntity parlorEntity = parlorRepository.findById(dto.getParlorId())
//                .orElseThrow(() -> new RuntimeException("Parlor Not Found"));
//        entity.setParlorEntity(parlorEntity);
//
//        UserEntity userEntity = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User Not Found"));
//        entity.setUserEntity(userEntity);
//
//        this.repository.save(entity);
//
//        return dto;
//    }

    public void delete(Long id) {
        this.repository.deleteById(id);
    }
}