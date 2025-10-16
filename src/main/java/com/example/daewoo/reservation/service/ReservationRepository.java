package com.example.daewoo.reservation.service;

import com.example.daewoo.reservation.dto.ReservationDto;
import com.example.daewoo.reservation.dto.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query("SELECT new com.example.daewoo.reservation.dto.ReservationDto(" + // ✨ DTO 패키지명 수정 필요
            "    r.reservationId," +
            "    r.parlorEntity.parId, " + // 예약 ID
            "    r.userEntity.userId," +
            "    r.parlorEntity.accRoomTypeEntity.accommodation.checkInTime, " +   // 체크인 시간 경로
            "    r.parlorEntity.accRoomTypeEntity.accommodation.checkOutTime," +
            "    r.checkIn," +
            "    r.checkOut) " +
            "FROM ReservationEntity r " +
            "WHERE r.userEntity.userId = :userId " + // ✨ userId를 통한 조건 추가
            "ORDER BY r.reservationId DESC") // 필요에 따라 정렬 조건 추가
    List<ReservationDto> findAllReservationsByUserIdWithCheckInOut(@Param("userId") Long userId);


}
