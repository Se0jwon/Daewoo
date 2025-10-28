package com.example.daewoo.parlor.roomtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDto {
    private Long roomTypeId;
    private String roomTypeName;
    private String parlorImage;

    public RoomTypeEntity toEntity(){
        RoomTypeEntity entity = new RoomTypeEntity();
        entity.setRoomTypeId(this.roomTypeId);
        entity.setRoomTypeName(this.roomTypeName);
        entity.setParlorImage(this.parlorImage);

        return entity;
    }

    public static RoomTypeDto fromEntity(RoomTypeEntity entity){
        RoomTypeDto dto = new RoomTypeDto();
        dto.setRoomTypeId(entity.getRoomTypeId());
        dto.setRoomTypeName(entity.getRoomTypeName());
        dto.setParlorImage(entity.getParlorImage());

        return dto;
    }

}
