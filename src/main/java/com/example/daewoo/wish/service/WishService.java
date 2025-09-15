package com.example.daewoo.wish.service;

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

    public WishDto insert(WishDto dto) {
        WishEntity entity = WishEntity.builder()
                .userId(dto.getUserId())
                .parlorId(dto.getParlorId())
                .build();
        WishEntity savedEntity = this.repository.save(entity);
        dto.setWishId(savedEntity.getWishId());
        return dto;
    }

    public List<WishDto> findAll() {
        return this.repository.findAll().stream()
                .map(entity -> new WishDto(entity.getWishId(), entity.getUserId(), entity.getParlorId()))
                .collect(Collectors.toList());
    }

    public Optional<WishDto> findById(Long id) {
        return this.repository.findById(id)
                .map(entity -> new WishDto(entity.getWishId(), entity.getUserId(), entity.getParlorId()));
    }

    public WishDto update(WishDto dto) {
        WishEntity entity = this.repository.findById(dto.getWishId())
                .orElseThrow(() -> new RuntimeException("Wish not found"));
        entity.setUserId(dto.getUserId());
        entity.setParlorId(dto.getParlorId());
        WishEntity updatedEntity = this.repository.save(entity);
        return new WishDto(updatedEntity.getWishId(), updatedEntity.getUserId(), updatedEntity.getParlorId());
    }

    public void delete(Long id) {
        this.repository.deleteById(id);
    }
}