package com.example.daewoo.parlor.service;

import com.example.daewoo.parlor.dto.ParlorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParlorRepository extends JpaRepository<ParlorEntity, Long> {
    @Query("SELECT COUNT(*) FROM ParlorEntity p WHERE p.accRoomTypeEntity.accId = :accId")
    public Integer countMaxRoomByParlor(@Param("accId") Long accId);

    List<ParlorEntity> findByAccRoomTypeEntityAccId(Long accId);
}