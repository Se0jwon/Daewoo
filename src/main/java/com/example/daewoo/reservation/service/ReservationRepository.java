package com.example.daewoo.reservation.service;

import com.example.daewoo.reservation.dto.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    public List<ReservationEntity> findByUserId(Long userId);
}
