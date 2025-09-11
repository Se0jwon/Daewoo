package com.example.daewoo.accommodation.dto;

import com.example.daewoo.accommodation.amenities.AmenitiesDto;
import com.example.daewoo.accommodation.location.dto.LocationDto;
import com.example.daewoo.review.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDto {
    private Long comId;
    private String comTitle;
    private String comDescription;
    private String comAddress;

    private List<AmenitiesDto> amenities;

    private LocationDto location;

    public AccommodationEntity toEntity(){
        AccommodationEntity entity = new AccommodationEntity();
        entity.setComId(this.comId);
        entity.setComTitle(this.comTitle);
        entity.setComDescription(this.comDescription);
        entity.setComAddress(this.comAddress);

        return entity;
    }

    public static AccommodationDto fromEntity(AccommodationEntity entity){
        AccommodationDto dto = new AccommodationDto();
        dto.setComId(entity.getComId());
        dto.setComTitle(entity.getComTitle());
        dto.setComDescription(entity.getComDescription());
        dto.setComAddress(entity.getComAddress());

        dto.setAmenities(entity.getAmenities().stream()
                .map(AmenitiesDto::fromEntity)
                .collect(Collectors.toList()));

        dto.setLocation(LocationDto.fromEntity(entity.getLocationEntity()));

        return dto;
    }
}
