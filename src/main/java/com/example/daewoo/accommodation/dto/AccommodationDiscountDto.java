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
public class AccommodationDiscountDto {
    private Long comId;
    private String comTitle;
    private Integer price; //원가
    private Integer discountedPrice; //할인가
    private String image;
    private String location;

    public AccommodationDiscountDto(Long comId, String comTitle, String image, Integer price, BigDecimal discountRate, String location) {
        this.comId = comId;
        this.comTitle = comTitle;
        this.image = image;
        this.price = price;
        this.location = location;

        // 생성자 안에서 할인가를 직접 계산합니다.
        if (price != null && discountRate != null && discountRate.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal priceBD = new BigDecimal(price);
            BigDecimal oneHundred = new BigDecimal("100");
            BigDecimal discountFactor = BigDecimal.ONE.subtract(discountRate.divide(oneHundred));
            this.discountedPrice = priceBD.multiply(discountFactor).intValue();
        } else {
            // 할인이 없으면 할인가격은 원가와 같습니다.
            this.discountedPrice = price;
        }
    }

    public AccommodationEntity toEntity(){
        AccommodationEntity entity = new AccommodationEntity();
        entity.setComId(this.comId);
        entity.setComTitle(this.comTitle);

        return entity;
    }

    public static AccommodationDiscountDto fromEntity(AccommodationEntity entity){
        AccommodationDiscountDto dto = new AccommodationDiscountDto();
        dto.setComId(entity.getComId());
        dto.setComTitle(entity.getComTitle());

        dto.setLocation(entity.getLocationEntity().getLocationName());

        return dto;
    }
}
