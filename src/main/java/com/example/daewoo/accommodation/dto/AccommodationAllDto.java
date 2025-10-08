package com.example.daewoo.accommodation.dto;

import com.example.daewoo.accommodation.amenities.AmenitiesDto;
import com.example.daewoo.accommodation.location.dto.LocationDto;
import com.example.daewoo.parlor.dto.ParlorDto;
import com.example.daewoo.parlor.roomtype.AccRoomTypeDto;
import com.example.daewoo.review.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationAllDto {
    private Long comId;
    private String comTitle;
    private String comAddress;
    private Integer star;
    private Integer price;
    private String image;
    private String category;
    private Boolean isFavorite;

    private BigDecimal reviewAvg;
    private Integer reviewCount;

    public AccommodationEntity toEntity(){
        AccommodationEntity entity = new AccommodationEntity();
        entity.setComId(this.comId);
        entity.setComTitle(this.comTitle);
        entity.setComAddress(this.comAddress);
        entity.setReviewAvg(this.reviewAvg);
        entity.setReviewCount(this.reviewCount);
        entity.setStar(this.star);
        entity.setCategory(this.category);

        return entity;
    }

    public static AccommodationAllDto fromEntity(AccommodationEntity entity){
        AccommodationAllDto dto = new AccommodationAllDto();
        dto.setComId(entity.getComId());
        dto.setComTitle(entity.getComTitle());
        dto.setComAddress(entity.getComAddress());
        dto.setReviewAvg(entity.getReviewAvg());
        dto.setReviewCount(entity.getReviewCount());
        dto.setStar(entity.getStar());
        dto.setCategory(entity.getCategory());

//        dto.setLocation(LocationDto.fromEntity(entity.getLocationEntity()));

        return dto;
    }
}
