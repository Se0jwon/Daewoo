// AccommodationService.java
package com.example.daewoo.accommodation.service;

import com.example.daewoo.accommodation.dto.AccommodationDto;
import com.example.daewoo.accommodation.dto.AccommodationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccommodationService {
    @Autowired
    private AccommodationRepository repository;

    public AccommodationDto insert(AccommodationDto dto) {
        AccommodationEntity entity = AccommodationEntity.builder()
                .name(dto.getName())
                .location(dto.getLocation())
                .price(dto.getPrice())
                .build();
        AccommodationEntity savedEntity = this.repository.save(entity);
        dto.setAccommodationId(savedEntity.getAccommodationId());
        return dto;
    }

    public List<AccommodationDto> findAll() {
        return this.repository.findAll().stream()
                .map(entity -> new AccommodationDto(entity.getAccommodationId(), entity.getName(), entity.getLocation(), entity.getPrice()))
                .collect(Collectors.toList());
    }

    public Optional<AccommodationDto> findById(Long id) {
        return this.repository.findById(id)
                .map(entity -> new AccommodationDto(entity.getAccommodationId(), entity.getName(), entity.getLocation(), entity.getPrice()));
    }

    public AccommodationDto update(AccommodationDto dto) {
        AccommodationEntity entity = this.repository.findById(dto.getAccommodationId())
                .orElseThrow(() -> new RuntimeException("Accommodation not found"));
        entity.setName(dto.getName());
        entity.setLocation(dto.getLocation());
        entity.setPrice(dto.getPrice());
        AccommodationEntity updatedEntity = this.repository.save(entity);
        return new AccommodationDto(updatedEntity.getAccommodationId(), updatedEntity.getName(), updatedEntity.getLocation(), updatedEntity.getPrice());
    }

    public void delete(Long id) {
        this.repository.deleteById(id);
    }
}