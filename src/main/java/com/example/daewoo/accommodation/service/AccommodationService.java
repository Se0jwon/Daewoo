package com.example.daewoo.accommodation.service;

import com.example.daewoo.accommodation.dto.AccommodationDto;
import com.example.daewoo.accommodation.dto.AccommodationEntity;

import com.example.daewoo.review.service.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.*;

@Service
public class AccommodationService {
    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public Page<AccommodationDto> findAll(Pageable pageable){
        Page<AccommodationEntity> entities = accommodationRepository.findAll(pageable);
        return entities.map(AccommodationDto::fromEntity);
    }

    public Optional<AccommodationDto> findById(Long id){
        Optional<AccommodationDto> accOpt = accommodationRepository.findById(id)
                .map(AccommodationDto::fromEntity);
        AccommodationDto dto = accOpt.get();

        dto.setReviewAvg(reviewRepository.findAverageScoreByComId(id).setScale(1, RoundingMode.HALF_UP));
        return Optional.of(dto);
    }
}