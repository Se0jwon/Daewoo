package com.example.daewoo.reservation.service;

import com.example.daewoo.parlor.dto.ParlorEntity;
import com.example.daewoo.parlor.service.ParlorRepository;
import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.reservation.dto.ReservationEntity;
import com.example.daewoo.user.dto.UserEntity;
import com.example.daewoo.user.service.UserRepository;
import com.example.daewoo.wish.dto.WishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ParlorRepository parlorRepository;

    @Autowired
    private UserRepository userRepository;

    public void insert(Long userId, ReservationDto dto){
        ReservationEntity entity = dto.toEntity();

        ParlorEntity parlorEntity = parlorRepository.findById(dto.getParId())
        .orElseThrow(() -> new RuntimeException("Parlor Not Found"));
        entity.setParlorEntity(parlorEntity);

        UserEntity userEntity = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setUserEntity(userEntity);

        this.reservationRepository.save(entity);
    }


    public List<ReservationDto> findByUserId(Long userId){
        return this.reservationRepository.findByUserEntity_UserId(userId).stream()
                .map(ReservationDto::fromEntity)
                .collect(Collectors.toList());


    }


    public void update(Long userId, ReservationDto dto){
        ReservationEntity entity = dto.toEntity();

        ParlorEntity parlorEntity = parlorRepository.findById(dto.getParId())
                .orElseThrow(() -> new RuntimeException("Parlor Not Found"));
        entity.setParlorEntity(parlorEntity);

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        entity.setUserEntity(userEntity);

        this.reservationRepository.save(entity);
    }

    public void delete(Long id){
        this.reservationRepository.deleteById(id);
    }
}
