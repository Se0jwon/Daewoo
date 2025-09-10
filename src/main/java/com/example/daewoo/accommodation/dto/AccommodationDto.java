package com.example.daewoo.accommodation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDto {
    private Long comId;
    private String comTitle;
    private String comDescription;
    private String comAddress;

    private Long locationId;
    private Long wishId;

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

        return dto;
    }
}
