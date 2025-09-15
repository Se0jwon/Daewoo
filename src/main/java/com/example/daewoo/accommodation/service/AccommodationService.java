package com.example.daewoo.accommodation.service;

import com.example.daewoo.accommodation.dto.AccommodationDto;
import com.example.daewoo.accommodation.dto.AccommodationEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccommodationService {
    @Autowired
    private AccommodationRepository accommodationRepository;

    public Page<AccommodationDto> findAll(Pageable pageable){
        Page<AccommodationEntity> entities = accommodationRepository.findAll(pageable);
        return entities.map(AccommodationDto::fromEntity);
    }

    public Optional<AccommodationDto> findById(Long id){
        return accommodationRepository.findById(id)
                .map(AccommodationDto::fromEntity);
    }
}
