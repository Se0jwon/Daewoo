package com.example.daewoo.parlor.roomtype.image.service;

import com.example.daewoo.accommodation.image.dto.ComImageEntity;
import com.example.daewoo.parlor.roomtype.RoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParlorImageRepository extends JpaRepository<RoomTypeEntity, Long> {
}
