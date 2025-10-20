package com.example.daewoo.parlor.roomtype;

import com.example.daewoo.accommodation.dto.PaymentAccommodationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAccRoomTypeDto {
    private Long accId;
    private Long comId;
    private PaymentAccommodationDto paymentAccommodation;
    private RoomTypeDto roomType;
    private Integer maxRoom;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public AccRoomTypeEntity toEntity(){
        AccRoomTypeEntity entity = new AccRoomTypeEntity();
        entity.setAccId(this.accId);
        entity.setMaxRoom(this.maxRoom);

        return entity;
    }

    public static PaymentAccRoomTypeDto fromEntity(AccRoomTypeEntity entity, PaymentAccommodationDto paymentAccommodation){
        PaymentAccRoomTypeDto dto = new PaymentAccRoomTypeDto();
        dto.setAccId(entity.getAccId());
        dto.setMaxRoom(entity.getMaxRoom());
        dto.setComId(entity.getAccommodation().getComId());

        dto.setRoomType(RoomTypeDto.fromEntity(entity.getRoomType()));
        dto.setPaymentAccommodation(paymentAccommodation);

        return dto;
    }
}
