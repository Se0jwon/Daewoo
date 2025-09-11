package com.example.daewoo.accommodation.accommodation;

import com.example.daewoo.accommodation.dto.AccommodationDto;
import com.example.daewoo.accommodation.dto.AccommodationEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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