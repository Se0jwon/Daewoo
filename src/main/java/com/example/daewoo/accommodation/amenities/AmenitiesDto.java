package com.example.daewoo.accommodation.amenities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class AmenitiesDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amId;

    private String amCategory;
    private String iconName;

    public AmenitiesEntity toEntity(){
        AmenitiesEntity entity = new AmenitiesEntity();
        entity.setAmId(this.amId);
        entity.setAmCategory(this.amCategory);
        entity.setIconName(this.iconName);

        return entity;
    }

    public static AmenitiesDto fromEntity(AmenitiesEntity entity){
        AmenitiesDto dto = new AmenitiesDto();
        dto.setAmId(entity.getAmId());
        dto.setAmCategory(entity.getAmCategory());
        dto.setIconName(entity.getIconName());

        return dto;
    }

}