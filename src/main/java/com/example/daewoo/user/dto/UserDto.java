package com.example.daewoo.user.dto;

import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.reservation.dto.ReservationEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    // Entity -> Dto
    public UserDto(UserEntity entity) {
        this.userId = entity.getUserId();
        this.username = entity.getUsername();
        this.password = entity.getPassword();
        this.userAddress = entity.getUserAddress();
        this.userPhone = entity.getUserPhone();
        this.userEmail = entity.getUserEmail();
        this.userBirth = entity.getUserBirth();
        if (entity.getReservations() != null) {
            this.reservations = entity.getReservations().stream()
                    .map(ReservationDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }
}