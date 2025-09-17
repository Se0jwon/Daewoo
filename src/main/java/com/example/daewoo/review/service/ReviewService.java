package com.example.daewoo.review.service;

import com.example.daewoo.accommodation.dto.AccommodationEntity;
import com.example.daewoo.accommodation.service.AccommodationRepository;
import com.example.daewoo.review.dto.ReviewDto;
import com.example.daewoo.review.dto.ReviewEntity;
import com.example.daewoo.user.dto.UserEntity;
import com.example.daewoo.user.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    public void insert(ReviewDto dto) {
        ReviewEntity entity = dto.toEntity();

        AccommodationEntity accommodationEntity = accommodationRepository.findById(dto.getComId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setAccommodationEntity(accommodationEntity);

        this.reviewRepository.save(entity);
    }

    public Page<ReviewDto> findAll(Pageable pageable){
        Page<ReviewEntity> entities = reviewRepository.findAll(pageable);

        return entities.map(ReviewDto::fromEntity);
    }

    public Optional<ReviewDto> findById(Long id){
        return reviewRepository.findById(id)
                .map(ReviewDto::fromEntity);
    }

    public void update(ReviewDto dto){
        ReviewEntity entity = dto.toEntity();

        AccommodationEntity accommodationEntity = accommodationRepository.findById(dto.getComId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setAccommodationEntity(accommodationEntity);

        this.reviewRepository.save(entity);
    }

    public void delete(Long id){
        this.reviewRepository.deleteById(id);
    }
}
