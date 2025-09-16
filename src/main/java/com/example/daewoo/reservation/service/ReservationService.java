package com.example.daewoo.reservation.service;

import com.example.daewoo.reservation.dto.ReservationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    public void insert(ReservationDto dto){
        this.reservationRepository.save(dto.toEntity());
    }
}
