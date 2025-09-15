package com.example.daewoo.parlor.dto;

import com.example.daewoo.accommodation.location.dto.LocationDto;
import com.example.daewoo.parlor.roomtype.AccRoomTypeDto;
import com.example.daewoo.parlor.roomtype.AccRoomTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParlorDto {
    private Long parId;
    private Long comId;
    private AccRoomTypeDto accRoomType;
    private String parContent;

    public ParlorEntity toEntity(){
        ParlorEntity entity = new ParlorEntity();
        entity.setParId(this.parId);
        entity.setParContent(this.parContent);

        return entity;
    }

    public static ParlorDto fromEntity(ParlorEntity entity){
        ParlorDto dto = new ParlorDto();
        dto.setParId(entity.getParId());
        dto.setComId(entity.getAccommodationEntity().getComId());
        dto.setParContent(entity.getParContent());

        dto.setAccRoomType(AccRoomTypeDto.fromEntity(entity.getAccRoomTypeEntity()));

        return dto;
    }
}
