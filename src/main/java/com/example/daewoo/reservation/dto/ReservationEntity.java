package com.example.daewoo.reservation.dto;

import com.example.daewoo.parlor.dto.ParlorEntity;
import com.example.daewoo.user.dto.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "par_id")
    private ParlorEntity parlorEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    private Boolean status;
    private LocalDate checkIn;
    private LocalDate checkOut;


}
