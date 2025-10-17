package com.example.daewoo.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long reservationId;
    private Long parId;
    private Long accId;
    private Long userId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    public ReservationDto(Long reservationId, Long parId, Long accId, Long userId, LocalTime checkInTime, LocalTime checkOutTime, LocalDate checkIn, LocalDate checkOut) {
        this.reservationId = reservationId;
        this.parId = parId;
        this.accId = accId;
        this.userId = userId;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
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
        dto.setAccId(entity.getParlorEntity().getAccRoomTypeEntity().getAccId());
        dto.setUserId(entity.getUserEntity().getUserId());
        dto.setCheckInTime(entity.getParlorEntity().getAccRoomTypeEntity().getAccommodation().getCheckInTime());
        dto.setCheckOutTime(entity.getParlorEntity().getAccRoomTypeEntity().getAccommodation().getCheckOutTime());

        return dto;
    }
}
