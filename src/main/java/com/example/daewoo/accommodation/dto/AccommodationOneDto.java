package com.example.daewoo.accommodation.dto;

import com.example.daewoo.accommodation.amenities.AmenitiesDto;
import com.example.daewoo.accommodation.location.dto.LocationDto;
import com.example.daewoo.parlor.dto.ParlorDto;
import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.review.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationOneDto {
    private Long comId;
    private String comTitle;
    private String comDescription;
    private String comAddress;
    private Integer star;


    private List<AmenitiesDto> amenities;

    private LocationDto location;

    private BigDecimal reviewAvg;
    private Integer reviewCount;
//    private List<ReviewDto> reviews;

    private List<ParlorDto> parlors;


    public AccommodationEntity toEntity(){
        AccommodationEntity entity = new AccommodationEntity();
        entity.setComId(this.comId);
        entity.setComTitle(this.comTitle);
        entity.setComDescription(this.comDescription);
        entity.setComAddress(this.comAddress);
        entity.setReviewAvg(this.reviewAvg);
        entity.setReviewCount(this.reviewCount);
        entity.setStar(this.star);

        return entity;
    }

    public static AccommodationOneDto fromEntity(AccommodationEntity entity){
        AccommodationOneDto dto = new AccommodationOneDto();
        dto.setComId(entity.getComId());
        dto.setComTitle(entity.getComTitle());
        dto.setComDescription(entity.getComDescription());
        dto.setComAddress(entity.getComAddress());
        dto.setReviewAvg(entity.getReviewAvg());
        dto.setReviewCount(entity.getReviewCount());
        dto.setStar(entity.getStar());

        dto.setAmenities(entity.getAmenities().stream()
                .map(AmenitiesDto::fromEntity)
                .collect(Collectors.toList()));

//        dto.setReviews(entity.getReviews().stream()
//                .map(ReviewDto::fromEntity)
//                .collect(Collectors.toList()));

        dto.setParlors(entity.getParlors().stream()
                .map(ParlorDto::fromEntity)
                .toList());

        dto.setLocation(LocationDto.fromEntity(entity.getLocationEntity()));

        return dto;
    }
}
