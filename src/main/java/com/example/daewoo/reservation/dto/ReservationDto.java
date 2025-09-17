package com.example.daewoo.reservation.dto;

import com.example.daewoo.parlor.dto.ParlorDto;
import com.example.daewoo.parlor.dto.ParlorEntity;
import com.example.daewoo.parlor.roomtype.AccRoomTypeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long reservationId;
    private Long parId;
    private Long userId;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public ReservationEntity toEntity(){
        ReservationEntity entity = new ReservationEntity();
        entity.setReservationId(this.reservationId);
        entity.setCheckIn(this.checkIn);
        entity.setCheckOut(this.checkOut);

        return entity;
    }

    public static ReservationDto fromEntity(ReservationEntity entity){
        ReservationDto dto = new ReservationDto();
        dto.setReservationId(entity.getReservationId());
        dto.setCheckIn(entity.getCheckIn());
        dto.setCheckOut(entity.getCheckOut());
        dto.setParId(entity.getParlorEntity().getParId());
        dto.setUserId(entity.getUserEntity().getUserId());

        return dto;
    }

}
