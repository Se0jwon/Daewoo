package com.example.daewoo.accommodation.dto;

import com.example.daewoo.accommodation.location.dto.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAccommodationDto {
    private Long comId;
    private String comTitle;
    private String comAddress;
    private Integer price;
    private BigDecimal reviewAvg;
    private Integer reviewCount;
    private String mainImage;

    public AccommodationEntity toEntity(){
        AccommodationEntity entity = new AccommodationEntity();
        entity.setComId(this.comId);
        entity.setComTitle(this.comTitle);
        entity.setComAddress(this.comAddress);
        entity.setReviewAvg(this.reviewAvg);
        entity.setReviewCount(this.reviewCount);

        return entity;
    }

    public static PaymentAccommodationDto fromEntity(AccommodationEntity entity, Integer price, String mainImage){
        PaymentAccommodationDto dto = new PaymentAccommodationDto();
        dto.setComId(entity.getComId());
        dto.setComTitle(entity.getComTitle());
        dto.setComAddress(entity.getComAddress());
        dto.setReviewAvg(entity.getReviewAvg());
        dto.setReviewCount(entity.getReviewCount());
        dto.setMainImage(mainImage);
        dto.setPrice(price);

        return dto;
    }
}
