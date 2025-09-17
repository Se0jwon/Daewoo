package com.example.daewoo.user.dto;

import com.example.daewoo.reservation.dto.ReservationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String username;
    private String password;
    private String userAddress;
    private String userPhone;
    private String userEmail;
    private LocalDate userBirth;

    private List<ReservationDto> reservations;

}