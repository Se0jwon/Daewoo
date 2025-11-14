package com.example.daewoo.parlor.service;

import com.example.daewoo.parlor.roomtype.AccRoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccRoomTypeRepository extends JpaRepository<AccRoomTypeEntity, Long> {

    @Query("SELECT art FROM AccRoomTypeEntity art " +
            "WHERE art.accommodation.comId = :comId " +
            "AND art.maxRoom > (" +
            "    SELECT COUNT(r.reservationId) FROM ReservationEntity r " +
            "    JOIN r.parlorEntity p " +
            "    WHERE p.accRoomTypeEntity = art " +
            "    AND r.checkIn < :checkOut AND r.checkOut > :checkIn" +
            ")")
    List<AccRoomTypeEntity> findAvailableRoomEntities(
            @Param("comId") Long comId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    @Query("SELECT a.price FROM AccRoomTypeEntity a WHERE a.accId = :accId")
    Integer findPriceByAccId(@Param("accId") Long accId);
}