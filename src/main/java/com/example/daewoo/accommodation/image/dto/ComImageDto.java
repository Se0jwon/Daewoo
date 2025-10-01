package com.example.daewoo.accommodation.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComImageDto {
    private Long comImageId;
    private String imageUrl;
    private Boolean isMain;
    private Long comId;

    public ComImageEntity toEntity(){
        ComImageEntity entity = new ComImageEntity();
        entity.setComImageId(this.comImageId);
        entity.setImageUrl(this.imageUrl);
        entity.setIsMain(this.isMain);

        return entity;
    }

    public static ComImageDto fromEntity(ComImageEntity entity){
        ComImageDto dto = new ComImageDto();
        dto.setComImageId(entity.getComImageId());
        dto.setImageUrl(entity.getImageUrl());
        dto.setIsMain(entity.getIsMain());
        dto.setComId(entity.getAccommodation().getComId());

        return dto;
    }
}
