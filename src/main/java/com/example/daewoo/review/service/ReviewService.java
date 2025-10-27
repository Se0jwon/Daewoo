package com.example.daewoo.review.service;

import com.example.daewoo.accommodation.dto.AccommodationAllDto;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private UserRepository userRepository;

    public void insert(ReviewDto dto) {
        ReviewEntity entity = dto.toEntity();

        UserEntity userEntity = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setUserEntity(userEntity);

        AccommodationEntity accommodationEntity = accommodationRepository.findById(dto.getComId())
                .orElseThrow(() -> new RuntimeException("Hotel Not Found"));

        entity.setAccommodationEntity(accommodationEntity);

        this.reviewRepository.save(entity);
        updateAccommodationReviewSummary(dto.getComId());
    }

    public Page<ReviewDto> findAllByAccommodationEntity_ComId(Long comId, Pageable pageable){
        Page<ReviewEntity> entities = reviewRepository.findAllByAccommodationEntity_ComId(comId, pageable);

        return entities.map(ReviewDto::fromEntity);
    }

    public Optional<ReviewDto> findById(Long id){
        return reviewRepository.findById(id)
                .map(ReviewDto::fromEntity);
    }

    public void update(ReviewDto dto){
        ReviewEntity entity = dto.toEntity();


        UserEntity userEntity = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setUserEntity(userEntity);

        AccommodationEntity accommodationEntity = accommodationRepository.findById(dto.getComId())
                .orElseThrow(() -> new RuntimeException("Hotel Not Found"));

        entity.setAccommodationEntity(accommodationEntity);

        this.reviewRepository.save(entity);
        updateAccommodationReviewSummary(dto.getComId());
    }

    // 리뷰 삭제 시 호출될 메서드
    public void delete(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId).orElseThrow();
        reviewRepository.deleteById(reviewId);

        updateAccommodationReviewSummary(review.getAccommodationEntity().getComId());
    }

    private void updateAccommodationReviewSummary(Long comId) {
        // 1. 레포지토리에서 평균 점수와 리뷰 수 계산
        // findAverageScoreByComId()는 리뷰가 없으면 null을 반환할 수 있으므로
        // Optional로 감싸서 안전하게 처리
        // 1. 레포지토리에서 평균 점수 (Double 타입)를 가져옵니다.
        Double avgScoreDouble = reviewRepository.findAverageScoreByComId(comId);

        // 2. null을 확인하고 BigDecimal로 안전하게 변환합니다.
        BigDecimal avgScore;
        if (avgScoreDouble == null) {
            avgScore = new BigDecimal("0.0"); // 리뷰가 없으면 0.0으로 설정
        } else {
            avgScore = new BigDecimal(avgScoreDouble) // Double을 BigDecimal로 변환
                    .setScale(1, RoundingMode.HALF_UP); // 소수점 첫째 자리까지 반올림
        }

        Integer reviewCount = reviewRepository.findReviewCountByComId(comId);

        // 2. Accommodation 엔티티를 찾아서 값 업데이트
        AccommodationEntity accommodation = accommodationRepository.findById(comId).orElseThrow();

        accommodation.setReviewAvg(avgScore);
        accommodation.setReviewCount(reviewCount);

        // 3. 변경된 엔티티 저장
        accommodationRepository.save(accommodation);
    }
}
