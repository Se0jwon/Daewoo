package com.example.daewoo.parlor.service;

import com.example.daewoo.parlor.dto.ParlorEntity;
import com.example.daewoo.parlor.roomtype.AccRoomTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccRoomTypeRepository extends JpaRepository<AccRoomTypeEntity, Long> {

}