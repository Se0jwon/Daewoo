package com.example.daewoo.accommodation.service;

import com.example.daewoo.accommodation.dto.AccommodationAllDto;
import com.example.daewoo.accommodation.dto.AccommodationEntity;

import com.example.daewoo.accommodation.dto.AccommodationOneDto;
import com.example.daewoo.accommodation.image.dto.ComImageDto;
import com.example.daewoo.accommodation.image.dto.ComImageEntity;
import com.example.daewoo.accommodation.image.service.ComImageRepository;
import com.example.daewoo.parlor.roomtype.AccRoomTypeEntity;
import com.example.daewoo.review.service.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class AccommodationService {
    @Autowired
    private ComImageRepository comImageRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    public Long totalCount(){
        return accommodationRepository.hotelCount();
    }

    public Slice<AccommodationAllDto> findAll(Integer minPrice,Integer maxPrice, List<String> amCategory, String comTitle, Integer star, Pageable pageable){
        Specification<AccommodationEntity> spec = AccommodationSpecification.hasPriceInRange(minPrice, maxPrice);
        spec = spec.and(AccommodationSpecification.hasAmenities(amCategory));
        spec = spec.and(AccommodationSpecification.hasName(comTitle));
        spec = spec.and(AccommodationSpecification.hasStar(star));

        Slice<AccommodationEntity> entities = accommodationRepository.findAll(spec, pageable);
        Slice<AccommodationAllDto> dto = entities.map(AccommodationAllDto::fromEntity);
        List<AccommodationAllDto> list = dto.getContent();
        for (AccommodationAllDto item : list) {
            Long comId = item.getComId();
            Integer price = accommodationRepository.findLowestPriceByHotelId(comId);
            String image = accommodationRepository.findMainComImage(comId);

            item.setImage(image);
            item.setPrice(price);
        }

        return dto;
    }

    public Optional<AccommodationOneDto> findById(Long comId){
        AccommodationEntity accommodation = accommodationRepository.findById(comId)
                .orElseThrow(() -> new RuntimeException("숙소를 찾을 수 없습니다."));


        Integer price = accommodationRepository.findLowestPriceByHotelId(comId);
        String mainImage = accommodationRepository.findMainComImage(comId);
        List<String> subImage = accommodationRepository.findSubComImage(comId);

        AccommodationOneDto dto = AccommodationOneDto.fromEntity(accommodation);
        dto.setPrice(price);
        dto.setMainImage(mainImage);
        dto.setSubImage(subImage);

        return Optional.of(dto);
    }
}