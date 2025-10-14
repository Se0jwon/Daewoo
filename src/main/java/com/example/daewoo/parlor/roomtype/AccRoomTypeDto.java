package com.example.daewoo.parlor.roomtype;

import com.example.daewoo.accommodation.amenities.AmenitiesDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccRoomTypeDto {
    private Long accId;
    private Long comId;
    private RoomTypeDto roomType;
    private Integer price; //원가
    private Integer maxRoom;

    private Integer discountedPrice; //할인가
    private BigDecimal discountRate;



    public AccRoomTypeEntity toEntity(){
        AccRoomTypeEntity entity = new AccRoomTypeEntity();
        entity.setAccId(this.accId);
        entity.setPrice(this.price);
        entity.setMaxRoom(this.maxRoom);

        return entity;
    }

    public static AccRoomTypeDto fromEntity(AccRoomTypeEntity entity){
        AccRoomTypeDto dto = new AccRoomTypeDto();
        dto.setAccId(entity.getAccId());
        dto.setPrice(entity.getPrice());
        dto.setMaxRoom(entity.getMaxRoom());
        dto.setComId(entity.getAccommodation().getComId());

        dto.setRoomType(RoomTypeDto.fromEntity(entity.getRoomType()));

        return dto;
    }
}
