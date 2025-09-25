package com.example.daewoo.accommodation.service;

import com.example.daewoo.accommodation.dto.AccommodationAllDto;
import com.example.daewoo.accommodation.dto.AccommodationEntity;

import com.example.daewoo.accommodation.dto.AccommodationOneDto;
import com.example.daewoo.review.service.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccommodationService {
    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public Long totalCount(){
        return accommodationRepository.hotelCount();
    }

//    public Slice<AccommodationAllDto> findAll(Pageable pageable){
//        Slice<AccommodationEntity> entities = accommodationRepository.findAll(pageable);
//
//        return entities.map(entity -> {
//            AccommodationAllDto dto = AccommodationAllDto.fromEntity(entity); // 기본값 설정
//            Long comId = entity.getComId();
//
//            // 👉 여기서 repository 접근해서 setXxx
//            BigDecimal avg = reviewRepository.findAverageScoreByComId(comId);
//            if (avg != null) {
//                dto.setReviewAvg(avg.setScale(1, RoundingMode.HALF_UP));
//            }
//
//            dto.setReviewCount(reviewRepository.findReviewCountByComId(comId));
//
//            return dto;
//        });
//    }
    public Slice<AccommodationAllDto> findAll(Integer minPrice,Integer maxPrice, List<String> amCategory, String comTitle, Integer star, Pageable pageable){
        Specification<AccommodationEntity> spec = AccommodationSpecification.hasPriceInRange(minPrice, maxPrice);
        spec = spec.and(AccommodationSpecification.hasAmenities(amCategory));
        spec = spec.and(AccommodationSpecification.hasName(comTitle));
        spec = spec.and(AccommodationSpecification.hasStar(star));
        Slice<AccommodationEntity> entities = accommodationRepository.findAll(spec, pageable);

        return entities.map(AccommodationAllDto::fromEntity);
    }

    public Optional<AccommodationOneDto> findById(Long id){
        return this.accommodationRepository.findById(id).map(AccommodationOneDto::fromEntity);
    }
}