package com.example.daewoo.review.service;

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
    private UserRepository userRepository;

    public void insert(ReviewDto dto) {
        ReviewEntity entity = dto.toEntity();

//        UserEntity userEntity = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new RuntimeException("User Not Found"));
//        entity.setUserEntity(userEntity);

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

//        UserEntity userEntity = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new RuntimeException("User Not Found"));
//        entity.setUserEntity(userEntity);

        this.reviewRepository.save(entity);
    }

    public void delete(Long id){
        this.reviewRepository.deleteById(id);
    }
}
